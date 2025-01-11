package com.machado001.packagemanagerinteractor.domain

import android.content.pm.PackageInfo
import kotlinx.coroutines.flow.Flow

interface PackageRepository {
    val packageInfos: Flow<List<PackageInfo>>
}