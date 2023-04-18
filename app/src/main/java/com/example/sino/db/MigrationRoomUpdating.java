package com.example.sino.db;

import androidx.room.migration.Migration;
import androidx.sqlite.db.SupportSQLiteDatabase;

public class MigrationRoomUpdating {

    public static Migration migrate(int oldVersion, int newVersion) {
        for (int migrateVersion = oldVersion + 1; migrateVersion <= newVersion; migrateVersion++) {
            upgrade(migrateVersion);
        }
        return null;
    }

    public static void upgrade(int migrateVersion) {
        switch (migrateVersion) {
            case 2:
                new androidx.room.migration.Migration(1, 2) {
                    @Override
                    public void migrate(SupportSQLiteDatabase database) {
                        // Since we didn't alter the table, there's nothing else to do here.
                    }
                };
                break;

            case 3:
                new androidx.room.migration.Migration(2, 3) {
                    @Override
                    public void migrate(SupportSQLiteDatabase database) {
                        // Since we didn't alter the table, there's nothing else to do here.
                    }
                };
                break;

            case 4:
                new androidx.room.migration.Migration(3, 4) {
                    @Override
                    public void migrate(SupportSQLiteDatabase database) {
                        // Since we didn't alter the table, there's nothing else to do here.
                    }
                };
                break;
        }
    }
}
