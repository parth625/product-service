package com.ecommerce.product_service.core.domain;

import java.io.Serializable;

public interface IBase<K extends Serializable> {
    K getPrimaryKey();
    void setPrimaryKey(K id);
    boolean getDeleteFlag();
    void setDeleteFlag(boolean deleteFlag);
}
