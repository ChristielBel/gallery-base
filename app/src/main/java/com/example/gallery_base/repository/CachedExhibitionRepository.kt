package com.example.gallery_base.repository

import com.example.gallery_base.data.Exhibition
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow

class CachedExhibitionRepository(private val networkRepo: ExhibitionRepositoryImpl,
                                 private val offlineRepo: OfflineExhibitionRepository) : ExhibitionRepository {
    override fun getAllExhibitions(): Flow<List<Exhibition>> = flow {
        try {
            // Пытаемся получить данные из сети
            val networkExhibitions = networkRepo.getAllExhibitions().first()

            // Обновляем локальную базу
            offlineRepo.deleteAllExhibitions()
            offlineRepo.insertAllExhibitions(networkExhibitions)

            emit(networkExhibitions)
        } catch (e: Exception) {
            // Если нет сети, используем локальные данные
            emit(offlineRepo.getAllExhibitions().first())
        }
    }

    override suspend fun insertExhibition(exhibition: Exhibition) {
        try {
            networkRepo.insertExhibition(exhibition)
            // После успешного сохранения в сети, сохраняем локально
            offlineRepo.insertExhibition(exhibition)
        } catch (e: Exception) {
            // Если нет сети, сохраняем только локально
            offlineRepo.insertExhibition(exhibition)
            throw Exception("Saved locally. Will sync when online: ${e.message}")
        }
    }

    override suspend fun insertAllExhibitions(list: List<Exhibition>) {
        try {
            networkRepo.insertAllExhibitions(list)
            offlineRepo.insertAllExhibitions(list)
        } catch (e: Exception) {
            offlineRepo.insertAllExhibitions(list)
            throw Exception("Saved locally. Will sync when online: ${e.message}")
        }
    }

    override suspend fun updateExhibition(exhibition: Exhibition) {
        try {
            networkRepo.updateExhibition(exhibition)
            offlineRepo.updateExhibition(exhibition)
        } catch (e: Exception) {
            offlineRepo.updateExhibition(exhibition)
            throw Exception("Updated locally. Will sync when online: ${e.message}")
        }
    }

    override suspend fun deleteExhibition(exhibition: Exhibition) {
        try {
            networkRepo.deleteExhibition(exhibition)
            offlineRepo.deleteExhibition(exhibition)
        } catch (e: Exception) {
            offlineRepo.deleteExhibition(exhibition)
            throw Exception("Deleted locally. Will sync when online: ${e.message}")
        }
    }

    override suspend fun deleteAllExhibitions() {
        try {
            networkRepo.deleteAllExhibitions()
            offlineRepo.deleteAllExhibitions()
        } catch (e: Exception) {
            offlineRepo.deleteAllExhibitions()
            throw Exception("Cleared locally. Will sync when online: ${e.message}")
        }
    }
}