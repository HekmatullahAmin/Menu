package com.example.menu.data

import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import java.nio.ByteBuffer

class ConsumableViewModel(private val consumableDao: ConsumableDao) : ViewModel() {

    private var _currentConsumable: MutableLiveData<Consumable> = MutableLiveData()
    val currentConsumable: LiveData<Consumable> get() = _currentConsumable

    private var _bindConsumable: MutableLiveData<Consumable?> = MutableLiveData()
    val bindConsumable: LiveData<Consumable?> get() = _bindConsumable

    fun updateCurrentConsumable(consumable: Consumable) {
        _currentConsumable.value = consumable
    }

    fun resetCurrentConsumable() {
        _currentConsumable = MutableLiveData()
    }

    fun updateBindConsumable(consumable: Consumable) {
        _bindConsumable.value = consumable
    }

    fun resetBindConsumable() {
        _bindConsumable.value = null
    }

    private fun insert(consumable: Consumable) {
        viewModelScope.launch {
            consumableDao.insert(consumable)
        }
    }

    private fun update(consumable: Consumable) {
        viewModelScope.launch {
            consumableDao.update(consumable)
        }
    }

    fun updateConsumable(
        consumableId: Int,
        consumableName: String,
        consumablePrice: String,
        consumableImageUrl: String,
        consumableDescription: String
    ) {
        val updatedConsumable = getUpdatedConsumableEntry(
            consumableId,
            consumableName,
            consumablePrice,
            consumableImageUrl,
            consumableDescription
        )
        update(updatedConsumable)
    }

    private fun getUpdatedConsumableEntry(
        consumableId: Int,
        consumableName: String,
        consumablePrice: String,
        consumableImageUrl: String,
        consumableDescription: String
    ): Consumable {
        return Consumable(
            id = consumableId,
            name = consumableName,
            price = consumablePrice.toDouble(),
            imageUrl = consumableImageUrl,
            description = consumableDescription
        )
    }

    fun delete(consumable: Consumable) {
        viewModelScope.launch {
            consumableDao.delete(consumable)
        }
    }

    fun getConsumable(id: Int): LiveData<Consumable> {
        return consumableDao.getConsumable(id).asLiveData()
    }

    fun getAllConsumables(): LiveData<List<Consumable>> {
        val listOfConsumable: LiveData<List<Consumable>> =
            consumableDao.getAllConsumables().asLiveData()
        listOfConsumable.observeForever { consumables ->
            if (consumables.isNotEmpty()) {
                _currentConsumable.value = consumables[0]
            }
        }
        return listOfConsumable
    }

    fun isEntryValid(
        name: String,
        price: String,
        currentDrawable: Drawable?,
        defaultDrawable: Drawable?
    ): Boolean {
        val currentBitmap = drawableToBitmap(currentDrawable)
        val defaultBitmap = drawableToBitmap(defaultDrawable)

        return name.isNotBlank() && price.isNotBlank() && !areBitmapsEqual(
            currentBitmap,
            defaultBitmap
        )

    }

    private fun drawableToBitmap(drawable: Drawable?): Bitmap? {
        if (drawable == null) return null
        if (drawable is BitmapDrawable) {
            return drawable.bitmap
        }

        val bitmap = Bitmap.createBitmap(
            drawable.intrinsicWidth,
            drawable.intrinsicHeight,
            Bitmap.Config.ARGB_8888
        )
        val canvas = Canvas(bitmap)
        drawable.setBounds(0, 0, canvas.width, canvas.height)
        drawable.draw(canvas)
        return bitmap
    }

    private fun areBitmapsEqual(bitmap1: Bitmap?, bitmap2: Bitmap?): Boolean {
        if (bitmap1 == null && bitmap2 == null) return true
        if (bitmap1 == null || bitmap2 == null) return false

        val buffer1 = ByteBuffer.allocate(bitmap1.byteCount)
        bitmap1.copyPixelsToBuffer(buffer1)

        val buffer2 = ByteBuffer.allocate(bitmap2.byteCount)
        bitmap2.copyPixelsToBuffer(buffer2)

        return buffer1.array().contentEquals(buffer2.array())
    }

    fun insertConsumable(
        consumableName: String,
        consumablePrice: String,
        consumableImage: String,
        consumableDescription: String
    ) {
        val consumable = getNewConsumableEntry(
            consumableName,
            consumablePrice,
            consumableImage,
            consumableDescription
        )
        insert(consumable)
    }

    private fun getNewConsumableEntry(
        consumableName: String, consumablePrice: String, consumableImageUrl: String,
        consumableDescription: String
    ): Consumable {
        return Consumable(
            name = consumableName.trim(),
            price = consumablePrice.trim().toDouble(),
            imageUrl = consumableImageUrl.trim(),
            description = consumableDescription.trim()
        )
    }
}

class ConsumableViewModelFactory(private val consumableDao: ConsumableDao) :
    ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ConsumableViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return ConsumableViewModel(consumableDao) as T
        }
        throw IllegalArgumentException("Unknown View Model class")
    }
}