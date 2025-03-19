package com.java.functional.FunctionalJava.streams;

import java.util.function.Supplier;

public class ItemExample {
    public static void main(String[] args) {
        // Create a sample item
        Item item = createSampleItem();

        // Using nullSafe to handle the chain
        String name = String.valueOf(nullSafe(() -> item.itemGroup().itemPackage().itemcode().itemName()));
        System.out.println("Item name: " + (name != null ? name : "Unknown Item"));

        // Test with null values in the chain
        Item nullItem = createItemWithNulls();
        String nullItemName = String.valueOf(nullSafe(() -> nullItem.itemGroup().itemPackage().itemcode().itemName()));
        System.out.println("Null item name: " + (nullItemName != null ? nullItemName : "Unknown Item"));
    }

    // Utility method to safely handle null values in method chains
    public static <T> T nullSafe(Supplier<T> supplier) {
        try {
            return supplier.get();
        } catch (NullPointerException e) {
            return null;
        }
    }

    // Helper method to create a sample item
    private static Item createSampleItem() {
        ItemName itemName = new ItemName("Laptop");
        ItemCode itemCode = new ItemCode("LT-001", itemName);
        ItemPackage itemPackage = new ItemPackage("Electronics", itemCode);
        ItemGroup itemGroup = new ItemGroup("Consumer Goods", itemPackage);
        return new Item(1, itemGroup);
    }

    // Helper method to create an item with nulls in the chain
    private static Item createItemWithNulls() {
        ItemGroup itemGroup = new ItemGroup("Consumer Goods", null);
        return new Item(2, itemGroup);
    }

    // DTO classes

    static class Item {
        private int id;
        private ItemGroup group;

        public Item(int id, ItemGroup group) {
            this.id = id;
            this.group = group;
        }

        public ItemGroup itemGroup() {
            return group;
        }

        public int getId() {
            return id;
        }
    }

    static class ItemGroup {
        private String groupName;
        private ItemPackage itemPackage;

        public ItemGroup(String groupName, ItemPackage itemPackage) {
            this.groupName = groupName;
            this.itemPackage = itemPackage;
        }

        public ItemPackage itemPackage() {
            return itemPackage;
        }

        public String getGroupName() {
            return groupName;
        }
    }

    static class ItemPackage {
        private String packageName;
        private ItemCode itemCode;

        public ItemPackage(String packageName, ItemCode itemCode) {
            this.packageName = packageName;
            this.itemCode = itemCode;
        }

        public ItemCode itemcode() {
            return itemCode;
        }

        public String getPackageName() {
            return packageName;
        }
    }

    static class ItemCode {
        private String code;
        private ItemName itemName;

        public ItemCode(String code, ItemName itemName) {
            this.code = code;
            this.itemName = itemName;
        }

        public ItemName itemName() {
            return itemName;
        }

        public String getCode() {
            return code;
        }
    }

    static class ItemName {
        private String name;

        public ItemName(String name) {
            this.name = name;
        }

        public String itemName() {
            return name;
        }
    }
}
