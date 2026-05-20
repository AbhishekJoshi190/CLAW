package com.claw.dao;
import com.claw.model.AuditLog;
import com.claw.model.MockDatabase;
import com.claw.util.DBUtil;
import java.sql.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
/**
 * Data Access Object for audit logging.
 * Integrates database persistence and seamless in-memory fallback.
 * &lt;p&gt;
 * This class is responsible for safely recording all critical system
 actions
 * (like registrations, checkouts, and admin role changes). If the
 MySQL
 * database is offline, it gracefully routes the log entries to a
 static RAM
 * list so that the application remains fully functional and tracked.
 * &lt;/p&gt;
 */
public class AuditLogDAO {
    /**
     * Logs a system event into the MySQL database.
     * If the database throws an SQLException (e.g. server is down), it
     prints an

     * error
     * to stderr and caches the log in the MockDatabase RAM list.
     *
     * @param username The actor who triggered the event (e.g., email
    address)
     * @param action The category of the event (e.g., &quot;Login&quot;, &quot;Order
    Placed&quot;)
     * @param details The human-readable specific details of the
    action
     */
    public static void logEvent(String username, String action, String
            details) {
        String sql = &quot;INSERT INTO system_logs (username, action,
                details) VALUES (?, ?, ?)&quot;;
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, username != null &amp;&amp; !username.isEmpty() ?
                    username : &quot;System&quot;);
            stmt.setString(2, action);
            stmt.setString(3, details);
            stmt.executeUpdate();
            System.out.println(&quot;CLAW System Log: [&quot; + action + &quot;] &quot; +
                    details);
        } catch (SQLException e) {
// Fallback to RAM MockDatabase
            System.err.println(&quot;CLAW System: SQL connection failed.
                    Logging event to MockDatabase in RAM instead.&quot;);
            MockDatabase.addAuditLog(new AuditLog(username, action,
                    details));
        }
    }
/**
 * Retrieves all system logs for the Administrative Dashboard.
 * Orders the results sequentially by the most recent timestamp
 descending.
 *
 * @return List of AuditLog objects fetched from SQL or fallback
Mock lists
 */
    public List&lt;AuditLog&gt; findAll() {
        List&lt;AuditLog&gt; logs = new ArrayList&lt;&gt;();
        String sql = &quot;SELECT * FROM system_logs ORDER BY created_at
        DESC&quot;;
        try (Connection conn = DBUtil.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                logs.add(new AuditLog(
                        rs.getInt(&quot;id&quot;),
                rs.getString(&quot;username&quot;),
                rs.getString(&quot;action&quot;),
                rs.getString(&quot;details&quot;),

                rs.getTimestamp(&quot;created_at&quot;)));
            }
            return logs;
        } catch (SQLException e) {
            System.err.println(&quot;CLAW System: SQL connection failed.
                    Falling back to MockDatabase for logs.&quot;);
// Fallback to RAM list, sort descending by date or id
            List&lt;AuditLog&gt; mockLogs = new
                    ArrayList&lt;&gt;(MockDatabase.getAuditLogs());
// Sort by Timestamp (latest first) or ID (largest first)
            Collections.reverse(mockLogs);
            return mockLogs;
        }
    }
}