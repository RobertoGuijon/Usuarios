package com.robertoguijon.login.data.database

import android.content.ContentValues
import android.content.Context
import androidx.room.OnConflictStrategy
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.robertoguijon.login.R
import com.robertoguijon.login.data.database.dao.ImagenesDao
import com.robertoguijon.login.data.database.dao.PermisosDao
import com.robertoguijon.login.data.database.dao.UsuariosDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    // Aqui se crea y provee la base de datos
    @Provides
    @Singleton
    fun provideAppDatabase(@ApplicationContext context: Context): AppDatabase {
        return Room.databaseBuilder(
            context,
            AppDatabase::class.java,
            "usuarios"
        ).addCallback(object : RoomDatabase.Callback() {
            override fun onCreate(db: SupportSQLiteDatabase) {
                super.onCreate(db)
                // Insertar usuario inicial
                val usuarioInicialContentValues = ContentValues().apply {
                    put("nombre", "Roberto Guijon")
                    put("correo", "robertoguijon27@gmail.com")
                    put("usuario", "RobertoGuijon27")
                    put("password", "Admin123#")
                    put("idPermiso", 1)
                    put("idImagen", 1)
                }
                db.insert("usuarios", OnConflictStrategy.IGNORE, usuarioInicialContentValues)

                // Insertar permisos iniciales
                val permisosIniciales = listOf(
                    ContentValues().apply {
                        put("idPermiso", 1)
                        put("permiso", "Administrador")
                    },
                    ContentValues().apply {
                        put("idPermiso", 2)
                        put("permiso", "Usuario")
                    }
                )
                permisosIniciales.forEach { permisoContentValues ->
                    db.insert("permisos", OnConflictStrategy.IGNORE, permisoContentValues)
                }

                // Insertar imagen por defecto
                val imagenPorDefecto = context.resources.openRawResource(R.raw.default_img).readBytes()
                val imagenInicialContentValues = ContentValues().apply {
                    put("idImagen", 1)
                    put("imagen", imagenPorDefecto)
                }
                db.insert("imagenes", OnConflictStrategy.IGNORE, imagenInicialContentValues)

            }
        }).build()
    }

    // Aqui se proveen los DAOs

    @Provides
    fun provideUsuariosDao(database: AppDatabase): UsuariosDao {
        return database.usuariosDao()
    }

    @Provides
    fun providePermisosDao(database: AppDatabase): PermisosDao {
        return database.permisosDao()
    }

    @Provides
    fun provideImagenesDao(database: AppDatabase): ImagenesDao {
        return database.imagenesDao()
    }
}