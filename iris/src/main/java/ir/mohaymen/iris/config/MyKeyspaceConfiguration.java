package ir.mohaymen.iris.config;

import ir.mohaymen.iris.auth.JwtService;
import ir.mohaymen.iris.code.ActivationCode;
import ir.mohaymen.iris.token.Token;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.convert.KeyspaceConfiguration;

public class MyKeyspaceConfiguration extends KeyspaceConfiguration {


    @Override
    public boolean hasSettingsFor(Class<?> type) {
        return true;
    }

    @Override
    public KeyspaceSettings getKeyspaceSettings(Class<?> type) {

        KeyspaceSettings keyspaceSettings = new KeyspaceSettings(type, "");
        keyspaceSettings.setTimeToLive(120L);//2 min
        if (type==ActivationCode.class) {
            keyspaceSettings.setTimeToLive(120L);//2 min
        } else if (type==Token.class) {
            keyspaceSettings.setTimeToLive(3600*24*30L);//30 days
        }

        return keyspaceSettings;
    }
}