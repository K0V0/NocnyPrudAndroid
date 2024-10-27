package space.kovo.nocnyprud2.backend.inits

import android.content.Context
import androidx.room.Room
import space.kovo.nocnyprud2.backend.configurations.AppDatabase
import space.kovo.nocnyprud2.backend.singletons.DB

class DBInit: Init {

    override fun init(context: Context) {
        DB.instance = Room
            .databaseBuilder(
                context,
                AppDatabase::class.java,
                "nocnyprud")
            //FIXME iba na vyvoj/testovanie, presunut vsetko co nie je UI na background thread !!!!!!!!!!!!!!!!!!!!!!!!!!
            .allowMainThreadQueries()
            //TODO odstranit po prvej verzii a pekne migracie
            .fallbackToDestructiveMigration()
            .build()
    }
}
