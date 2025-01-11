package com.machado001.packagemanagerinteractor.data

import android.content.pm.PackageManager
import com.machado001.packagemanagerinteractor.domain.Package
import com.machado001.packagemanagerinteractor.domain.PackageRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class AndroidPackageRepository(private val packageManager: PackageManager) : PackageRepository {

    override val packageInfos: Flow<List<Package>> = flow {
        emit(packageManager.getInstalledPackages(PackageManager.GET_META_DATA).map {
            Package(
                name = it.packageName,
                version = it.versionName
            )
        })
    }
}