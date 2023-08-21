package com.github.andiim.plantscan.domain

/*class PlantInteractor
@Inject
constructor(private val plantRepo: PlantRepository, private val cameraRepo: CameraRepository) :
    PlantUseCase {
  override fun getPlantTypes(): PagingSource<Int, PlantType> = plantRepo.getPlantTypes()

  override fun getPlantsByType(type: String): PagingSource<Int, Plant> =
      plantRepo.getPlantsByType(type)

  override fun getAllPlant(): Flow<PagingData<Plant>> = plantRepo.getAllPlant()
  override fun getPlantDetail(id: String): Flow<Resource<Plant>> = plantRepo.getPlantDetail(id)

  override fun searchPlant(query: String): PagingSource<Int, Plant> = plantRepo.searchPlant(query)

  override fun getGarden(): Flow<List<Plant>> = plantRepo.getGarden()

  override fun isAddedToGarden(id: String): Flow<Boolean> = plantRepo.isAddedToGarden(id)

  override fun addPlantToGarden(plant: Plant) = plantRepo.addPlantToGarden(plant)

  override fun removePlantFromGarden(plant: Plant) = plantRepo.removePlantFromGarden(plant)
  override fun notifyImageCreated(savedUri: Uri) = cameraRepo.notifyImageCreated(savedUri)
  override fun createImageOutputFile(): File = cameraRepo.createImageOutputFile()
}*/
