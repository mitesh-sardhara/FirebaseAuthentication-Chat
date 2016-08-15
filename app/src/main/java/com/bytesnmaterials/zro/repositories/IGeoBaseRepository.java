package com.bytesnmaterials.zro.repositories;

import com.bytesnmaterials.zro.listeners.LoadingListener;

/**
 * An interface class for GeoBaseRepository.
 *
 * @author mitesh
 * @version 1.0
 * @since 25/7/16
 */
public interface IGeoBaseRepository {

    void getCountryFromIp(LoadingListener loadingListener);
}
