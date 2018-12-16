package com.sudo.campusambassdor.model;


import com.sudo.campusambassdor.enums.ItemType;

public interface LazyLoading {
    ItemType getItemType();
    void setItemType(ItemType itemType);
}
