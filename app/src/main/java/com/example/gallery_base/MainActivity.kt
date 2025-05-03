package com.example.gallery_base

import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import androidx.activity.addCallback
import androidx.appcompat.app.AppCompatActivity
import com.example.gallery_base.data.Painting
import com.example.gallery_base.fragment.ArtistFragment
import com.example.gallery_base.fragment.ExhibitionFragment
import com.example.gallery_base.fragment.PaintingFragment
import java.util.UUID

class MainActivity : AppCompatActivity() {
    interface Edit {
        fun append()
        fun update()
        fun delete()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d("MainActivity", "onCreate")
        setContentView(R.layout.activity_main)
        Log.d("MainActivity", "setContentView done")
        onBackPressedDispatcher.addCallback(this) {
            if (supportFragmentManager.backStackEntryCount > 0) {
                supportFragmentManager.popBackStack()
                when (activeFragment) {
                    NamesOfFragment.EXHIBITION -> {
                        finish()
                    }

                    NamesOfFragment.ARTIST -> {
                        activeFragment = NamesOfFragment.EXHIBITION
                    }

            NamesOfFragment.PAINTING -> {
            activeFragment = NamesOfFragment.ARTIST
        }

            else -> {}
        }
        updateMenu(activeFragment)
    } else {
                finish()
            }
        }

        showFragment(activeFragment, null)
        Log.d("MainActivity", "showFragment called")
    }

    private var activeFragment: NamesOfFragment = NamesOfFragment.EXHIBITION

    private var _miAppendExhibition: MenuItem? = null
    private var _miUpdateExhibition: MenuItem? = null
    private var _miDeleteExhibition: MenuItem? = null
    private var _miAppendArtist: MenuItem? = null
    private var _miUpdateArtist: MenuItem? = null
    private var _miDeleteArtist: MenuItem? = null

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        _miAppendExhibition = menu?.findItem(R.id.miAppendExhibition)
        _miUpdateExhibition = menu?.findItem(R.id.miUpdateExhibition)
        _miDeleteExhibition = menu?.findItem(R.id.miDeleteExhibition)
        _miAppendArtist = menu?.findItem(R.id.miAppendArtist)
        _miUpdateArtist = menu?.findItem(R.id.miUpdateArtist)
        _miDeleteArtist = menu?.findItem(R.id.miDeleteArtist)
        updateMenu(activeFragment)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val currentFragment = supportFragmentManager.findFragmentById(R.id.fcMain)
        val edit = currentFragment as? Edit

        return when (item.itemId) {
            R.id.miAppendExhibition, R.id.miAppendArtist -> {
                edit?.append()
                true
            }
            R.id.miUpdateExhibition, R.id.miUpdateArtist -> {
                edit?.update()
                true
            }
            R.id.miDeleteExhibition, R.id.miDeleteArtist -> {
                edit?.delete()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    fun newTitle(_title: String) {
        title = _title
    }

    private fun updateMenu(fragmentType: NamesOfFragment) {
        _miAppendExhibition?.isVisible = fragmentType == NamesOfFragment.EXHIBITION
        _miUpdateExhibition?.isVisible = fragmentType == NamesOfFragment.EXHIBITION
        _miDeleteExhibition?.isVisible = fragmentType == NamesOfFragment.EXHIBITION
        _miAppendArtist?.isVisible = fragmentType == NamesOfFragment.ARTIST
        _miUpdateArtist?.isVisible = fragmentType == NamesOfFragment.ARTIST
        _miDeleteArtist?.isVisible = fragmentType == NamesOfFragment.ARTIST
    }

    private var selectedExhibitionId: UUID? = null

    fun showFragment(fragmentType: NamesOfFragment, painting: Painting?, exhibitionId: UUID? = null) {
        val fragmentTransaction = supportFragmentManager.beginTransaction()

        when (fragmentType) {
            NamesOfFragment.EXHIBITION -> {
                fragmentTransaction.replace(R.id.fcMain, ExhibitionFragment.newInstance())
                selectedExhibitionId = null
            }
            NamesOfFragment.ARTIST -> {
                val id = exhibitionId ?: selectedExhibitionId
                if (id != null) {
                    fragmentTransaction.replace(R.id.fcMain, ArtistFragment.newInstance(id))
                    selectedExhibitionId = id
                } else {
                    Log.e("MainActivity", "exhibitionId is null when opening ArtistFragment")
                    return
                }
            }
            NamesOfFragment.PAINTING -> {
                painting?.let {
                    fragmentTransaction.replace(R.id.fcMain, PaintingFragment.newInstance(it.id))
                }
            }
        }

        fragmentTransaction.addToBackStack(null).commit()
        activeFragment = fragmentType
        updateMenu(fragmentType)
    }

    override fun onPostCreate(savedInstanceState: Bundle?) {
        super.onPostCreate(savedInstanceState)
        showFragment(activeFragment, null)
    }

    override fun onDestroy() {
        super.onDestroy()
    }
}

