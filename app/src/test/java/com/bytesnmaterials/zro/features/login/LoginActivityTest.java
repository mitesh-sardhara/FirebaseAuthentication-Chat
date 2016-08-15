package com.bytesnmaterials.zro.features.login;

import android.test.ActivityTestCase;

import com.bytesnmaterials.zro.services.AuthService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import static org.mockito.Mockito.when;

/**
 * Created by mitesh on 27/7/16.
 */
@RunWith(MockitoJUnitRunner.class)
public class LoginActivityTest extends ActivityTestCase {

    //Create instance of ILoginView
    @Mock
    ILoginView loginView;

    //Create AuthService class object
    @Mock
    AuthService authService;


    @Before
    public void setUp() throws Exception {
        super.setUp();
    }

    @Test
    public void testClickLogin() throws Exception {
        when(loginView.getEmail()).thenReturn("test2@gmail.com");
        when(loginView.getPassword()).thenReturn("test123#");
        authService.Login(loginView);

    }

}