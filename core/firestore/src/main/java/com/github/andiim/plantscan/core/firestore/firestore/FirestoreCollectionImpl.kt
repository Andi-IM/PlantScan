package com.github.andiim.plantscan.core.firestore.firestore

import com.github.andiim.plantscan.core.firestore.model.ClassCollectionData
import com.github.andiim.plantscan.core.firestore.model.FamilyCollectionData
import com.github.andiim.plantscan.core.firestore.model.GenusCollectionData
import com.github.andiim.plantscan.core.firestore.model.OrderCollectionData
import com.github.andiim.plantscan.core.firestore.model.PhylumCollectionData
import com.github.andiim.plantscan.core.firestore.model.PlantCollectionData
import com.github.andiim.plantscan.core.firestore.model.PlantDetailCollectionData
import com.github.andiim.plantscan.core.firestore.model.PlantQuery
import com.github.andiim.plantscan.core.firestore.utils.Constants.CLASSES
import com.github.andiim.plantscan.core.firestore.utils.Constants.FAMILIES
import com.github.andiim.plantscan.core.firestore.utils.Constants.GENUSES
import com.github.andiim.plantscan.core.firestore.utils.Constants.NAME
import com.github.andiim.plantscan.core.firestore.utils.Constants.ORDERS
import com.github.andiim.plantscan.core.firestore.utils.Constants.PHYLUM
import com.github.andiim.plantscan.core.firestore.utils.Constants.PLANTS
import com.github.andiim.plantscan.core.firestore.utils.Constants.PLANT_DETAILS
import com.github.andiim.plantscan.model.data.Taxonomy
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.toObject
import javax.inject.Inject
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.tasks.await

class FirestoreCollectionImpl @Inject constructor(private val firestore: FirebaseFirestore) :
    FirestoreCollections {
  override fun getPlants(query: PlantQuery?): Flow<List<PlantCollectionData>> = callbackFlow {
    val snapshotListener =
        firestore
            .collection(PLANTS)
            .orderBy(NAME)
            .addSnapshotListener { snapshot, e ->
              val plantResponse =
                  if (snapshot != null) {
                    val plants = snapshot.toObjects(PlantCollectionData::class.java)
                    plants
                  } else {
                    throw Exception(e)
                  }
              trySend(plantResponse)
            }
    awaitClose { snapshotListener.remove() }
  }
  override fun getDetailFromId(id: String): Flow<PlantCollectionData> = flow {
    val plantSnapshot = firestore.collection(PLANTS).document(id).get().await()
    val data = plantSnapshot.toObject<PlantCollectionData>()

    val detail = getDetail(id).first()
    val genus = getPlantGenus(detail.classification).first()
    val phylum = getPhylum(genus).first()
    val cls = getClass(genus).first()
    val ord = getOrders(genus).first()
    val fam = getFamilies(genus).first()

    val newDetail =
        detail.copy(
            taxonomy =
                Taxonomy(
                    id = genus.id,
                    phylum = phylum.name,
                    className = cls.name,
                    order = ord.name,
                    family = fam.name,
                    genus = genus.name,
                ),
        )

    data?.let { emit(it.copy(detail = newDetail)) } ?: throw Exception("Document not found!")
  }

  private fun getDetail(id: String): Flow<PlantDetailCollectionData> = flow {
    val snapshot = firestore.collection(PLANT_DETAILS).document(id).get().await()
    val data = snapshot.toObject<PlantDetailCollectionData>()
    data?.let { emit(it) } ?: throw Exception("Detail not found!")
  }

  private fun getPlantGenus(classificationId: String): Flow<GenusCollectionData> = flow {
    val snapshot = firestore.collection(GENUSES).document(classificationId).get().await()
    val data = snapshot.toObject<GenusCollectionData>()
    data?.let { emit(it) } ?: throw Exception("Genus not found!")
  }
  private fun getPhylum(genusData: GenusCollectionData): Flow<PhylumCollectionData> = flow {
    val snapshot = firestore.collection(PHYLUM).document(genusData.phylumRef).get().await()
    val data = snapshot.toObject<PhylumCollectionData>()
    data?.let { emit(it) } ?: throw Exception("Phylum not found!")
  }

  private fun getClass(genusData: GenusCollectionData): Flow<ClassCollectionData> = flow {
    val snapshot =
        firestore
            .collection(PHYLUM)
            .document(genusData.phylumRef)
            .collection(CLASSES)
            .document(genusData.classRef)
            .get()
            .await()
    val data = snapshot.toObject<ClassCollectionData>()
    data?.let { emit(it) } ?: throw Exception("Classname not found!")
  }

  private fun getOrders(genusData: GenusCollectionData): Flow<OrderCollectionData> = flow {
    val snapshot = firestore.collection(ORDERS).document(genusData.orderRef).get().await()
    val data = snapshot.toObject<OrderCollectionData>()
    data?.let { emit(it) } ?: throw Exception("Order not found!")
  }

  private fun getFamilies(genusData: GenusCollectionData): Flow<FamilyCollectionData> = flow {
    val snapshot =
        firestore
            .collection(ORDERS)
            .document(genusData.orderRef)
            .collection(FAMILIES)
            .document(genusData.familyRef)
            .get()
            .await()
    val data = snapshot.toObject<FamilyCollectionData>()
    data?.let { emit(it) } ?: throw Exception("Family not found!")
  }
}
