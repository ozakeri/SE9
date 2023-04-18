package com.example.sino.di;


import android.app.Application;

import androidx.room.Room;
import com.example.sino.db.SinoDao;
import com.example.sino.db.SinoDatabase;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import dagger.hilt.InstallIn;
import dagger.hilt.components.SingletonComponent;

@Module
@InstallIn(SingletonComponent.class)
public class DataBaseModule {

    @Provides
    @Singleton
    public static SinoDatabase provideDatabase(Application application) {
        return Room.databaseBuilder(application, SinoDatabase.class, "user")
                .allowMainThreadQueries()
                .fallbackToDestructiveMigration()
                //.addMigrations(MigrationRoomUpdating.migrate(1,2)).allowMainThreadQueries()
                .build();
    }

    @Provides
    @Singleton
    public static SinoDao provideSinoDao(SinoDatabase sinoDatabase) {
        return sinoDatabase.sinoDao();
    }

}
