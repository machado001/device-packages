package com.machado001.packagemanagerinteractor.domain


import kotlinx.coroutines.flow.Flow

interface PackageRepository {
    val packageInfos: Flow<List<Package>>
}