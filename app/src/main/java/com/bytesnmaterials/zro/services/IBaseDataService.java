package com.bytesnmaterials.zro.services;

/**
 * An interface class for BaseDataService.
 *
 * @author mitesh
 * @version 1.0
 * @since 22/7/16
 */
public interface IBaseDataService {

    void AddError(int code, boolean toLog);

    void AddSuccess(int code);
}
