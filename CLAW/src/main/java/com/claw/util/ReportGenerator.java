package com.claw.util;

import com.claw.model.Order;

import java.util.*;

/**
 * ReportGenerator processes list of system orders and compiles them
 * into structured analytics, sales metrics, and a Profit & Loss statement.
 */
public class ReportGenerator {

    /**
     * Container class for all parsed and computed report data.
     * Contains the raw math output that is formatted and displayed in the Admin
     * JSP.
     */
    public static class ReportData {
        // --- Summary Metrics ---
        public int totalOrdersCount = 0;
        public int deliveredOrdersCount = 0;
        public int pendingOrdersCount = 0;

        /** Total value of ALL orders placed, regardless of delivery status. */
        public double grossSales = 0.0;

        /** Sourced funds/value of ONLY completed and DELIVERED orders. */
        public double realizedRevenue = 0.0;

        /** The average checkout total of delivered orders. */
        public double averageOrderValue = 0.0;

        // Payment Breakdown
        public Map<String, Double> revenueByPaymentMethod = new HashMap<>();
        public Map<String, Integer> countByPaymentMethod = new HashMap<>();

        // Best Sellers (Product Name -> Total Quantity Sold)
        public Map<String, Integer> bestSellers = new LinkedHashMap<>();

        // Profit & Loss
        public double cogs = 0.0; // Cost of Goods Sold (50% of Realized Revenue)
        public double grossProfit = 0.0;

        // Expenses
        public double expenseHosting = 1200.0; // Fixed Server cost
        public double expenseLogistics = 0.0; // 8% of realized sales
        public double expensePackaging = 0.0; // NPR 150 per delivered order
        public double expenseMarketing = 0.0; // 12% of realized sales
        public double totalExpenses = 0.0;

        // Bottom Line
        public double netProfit = 0.0;
        public double netProfitMargin = 0.0;

        // --- GETTERS REQUIRED FOR JSP EL RENDERING ---
        public int getTotalOrdersCount() {
            return totalOrdersCount;
        }

        public int getDeliveredOrdersCount() {
            return deliveredOrdersCount;
        }

        public int getPendingOrdersCount() {
            return pendingOrdersCount;
        }

        public double getGrossSales() {
            return grossSales;
        }

        public double getRealizedRevenue() {
            return realizedRevenue;
        }

        public double getAverageOrderValue() {
            return averageOrderValue;
        }

        public Map<String, Double> getRevenueByPaymentMethod() {
            return revenueByPaymentMethod;
        }

        public Map<String, Integer> getCountByPaymentMethod() {
            return countByPaymentMethod;
        }

        public Map<String, Integer> getBestSellers() {
            return bestSellers;
        }

        public double getCogs() {
            return cogs;
        }

        public double getGrossProfit() {
            return grossProfit;
        }

        public double getExpenseHosting() {
            return expenseHosting;
        }

        public double getExpenseLogistics() {
            return expenseLogistics;
        }

        public double getExpensePackaging() {
            return expensePackaging;
        }

        public double getExpenseMarketing() {
            return expenseMarketing;
        }

        public double getTotalExpenses() {
            return totalExpenses;
        }

        public double getNetProfit() {
            return netProfit;
        }

        public double getNetProfitMargin() {
            return netProfitMargin;
        }
    }

    /**
     * Generates a complete financial and inventory analysis report from standard
     * system orders.
     */
    public static ReportData generateReport(List<Order> orders) {
        ReportData report = new ReportData();
        if (orders == null || orders.isEmpty()) {
            return report;
        }

        report.totalOrdersCount = orders.size();
        Map<String, Integer> rawBestSellers = new HashMap<>();

        for (Order order : orders) {
            double amount = order.getTotalAmount();
            report.grossSales += amount;

            boolean isDelivered = "DELIVERED".equals(order.getStatus());
            if (isDelivered) {
                report.deliveredOrdersCount++;
                report.realizedRevenue += amount;
            } else {
                report.pendingOrdersCount++;
            }

            // Payment Methods calculation
            String method = order.getPaymentMethod();
            if (method == null || method.isEmpty())
                method = "COD";
            method = method.toUpperCase();

            report.countByPaymentMethod.put(method, report.countByPaymentMethod.getOrDefault(method, 0) + 1);
            if (isDelivered) {
                report.revenueByPaymentMethod.put(method,
                        report.revenueByPaymentMethod.getOrDefault(method, 0.0) + amount);
            }

            // Parse Items for Best Sellers
            String summary = order.getItemsSummary();
            if (summary != null && !summary.isEmpty()) {
                String[] items = summary.split(", ");
                for (String item : items) {
                    item = item.trim();
                    if (item.isEmpty())
                        continue;

                    int qty = 1;
                    String name = item;

                    if (item.contains("x ")) {
                        int idx = item.indexOf("x ");
                        try {
                            qty = Integer.parseInt(item.substring(0, idx).trim());
                            name = item.substring(idx + 2).trim();
                        } catch (NumberFormatException e) {
                            // default to 1 and original string
                        }
                    }

                    // Strip optional details like size or customization tags
                    if (name.contains(" (Size:")) {
                        name = name.substring(0, name.indexOf(" (Size:")).trim();
                    }
                    if (name.contains(" (Hips:")) {
                        name = name.substring(0, name.indexOf(" (Hips:")).trim();
                    }
                    if (name.contains(" [Custom")) {
                        name = name.substring(0, name.indexOf(" [Custom")).trim();
                    }

                    rawBestSellers.put(name, rawBestSellers.getOrDefault(name, 0) + qty);
                }
            }
        }

        // Sort best sellers descending by quantity sold
        List<Map.Entry<String, Integer>> list = new ArrayList<>(rawBestSellers.entrySet());
        list.sort((a, b) -> b.getValue().compareTo(a.getValue()));
        for (Map.Entry<String, Integer> entry : list) {
            report.bestSellers.put(entry.getKey(), entry.getValue());
        }

        // Metrics calculations
        if (report.deliveredOrdersCount > 0) {
            report.averageOrderValue = report.realizedRevenue / report.deliveredOrdersCount;
        } else {
            report.averageOrderValue = 0.0;
        }

        // --- PROFIT & LOSS COMPUTATIONS ---
        // Calculations are strictly based on Delivered Orders (realized revenue) to
        // ensure accuracy

        // Cost of Goods Sold (COGS): We estimate a standard 50% retail sourcing margin.
        // E.g., a shirt sold for NPR 2,000 cost NPR 1,000 to manufacture/purchase.
        report.cogs = report.realizedRevenue * 0.50;

        // Gross Profit: What we made after paying for the physical products.
        report.grossProfit = report.realizedRevenue - report.cogs;

        // --- SIMULATED OPERATING OVERHEAD ---
        // 1. Logistics: Estimated courier and shipping fees (8% of realized revenue)
        report.expenseLogistics = report.realizedRevenue * 0.08;

        // 2. Packaging: Premium CLAW streetwear boxes, custom labels, and tags (Fixed
        // NPR 150 per order)
        report.expensePackaging = report.deliveredOrdersCount * 150.0;

        // 3. Marketing: Facebook/Instagram Ads, influencer promos, and loyalty points
        // (12% of realized revenue)
        report.expenseMarketing = report.realizedRevenue * 0.12;

        // Combine all expenses (including fixed hosting overhead)
        report.totalExpenses = report.expenseHosting + report.expenseLogistics + report.expensePackaging
                + report.expenseMarketing;

        // Net Profit: The final "take-home" bottom-line income.
        report.netProfit = report.grossProfit - report.totalExpenses;

        // Net Profit Margin: The percentage of revenue that turns into profit.
        if (report.realizedRevenue > 0) {
            report.netProfitMargin = (report.netProfit / report.realizedRevenue) * 100.0;
        } else {
            report.netProfitMargin = 0.0;
        }

        return report;
    }
}
