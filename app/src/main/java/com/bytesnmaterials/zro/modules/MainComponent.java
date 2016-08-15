package com.bytesnmaterials.zro.modules;

import com.bytesnmaterials.zro.features.chat.ChatActivity;
import com.bytesnmaterials.zro.features.chat.UsersChatActivity;
import com.bytesnmaterials.zro.features.login.LoginActivity;
import com.bytesnmaterials.zro.features.register.RegisterActivity;
import com.bytesnmaterials.zro.fragments.FragmentUsers;

import javax.inject.Singleton;

import dagger.Component;

@Singleton
@Component(
        modules = {AppModule.class}
)
public interface MainComponent {

    void inject(LoginActivity activity);

    void inject(RegisterActivity registerActivity);

    void inject(ChatActivity activity);

    void inject(UsersChatActivity activity);

}