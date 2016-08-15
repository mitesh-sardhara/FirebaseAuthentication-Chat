package com.bytesnmaterials.zro.services;

import android.content.Context;
import com.bytesnmaterials.zro.R;
import java.util.ArrayList;

/**
 * This class is used as base class for Auth Service & User Service.
 *
 * @author mitesh
 * @version 1.0
 * @since 22/7/16
 */
public class BaseDataService implements IBaseDataService{

    public ArrayList<String> errors;

    public BaseDataService() {
        errors = new ArrayList<String>();
    }

    @Override
    public void AddError(int code, boolean toLog) {
        ZeroErrorService error = ZeroErrorService.fromCode(code);
        errors.add(error.getMessage());
        if (toLog) error.doLog();
    }

    @Override
    public void AddSuccess(int code) {
        ZeroSuccessService sucessService = ZeroSuccessService.fromCode(code);
        errors.add(sucessService.getMessage());
    }

}
