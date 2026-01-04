package com.mudrichenkoevgeny.backend.core.common.config.pathresolver

import java.io.File
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
    class PathResolverImpl @Inject constructor(
    pathResolverConfig: PathResolverConfig
) : PathResolver {

    private val envFile: File
    private val secretsDir: File

    init {
        val secretsPath = pathResolverConfig.secretsDirPath
            ?: throw IllegalStateException(
                "Required environment variable '${pathResolverConfig.secretsDirPath}' is missing"
            )
        secretsDir = resolveFile(pathResolverConfig.projectRoot, secretsPath)
        if (!secretsDir.exists()) {
            throw NoSuchFileException(secretsDir)
        }

        val envFilePath = pathResolverConfig.envFilePath
            ?: throw IllegalStateException(
                "Required environment variable '${pathResolverConfig.envFilePath}' is missing"
            )
        envFile = resolveFile(pathResolverConfig.projectRoot, envFilePath)
        if (!envFile.exists()) {
            throw NoSuchFileException(envFile)
        }
    }

    override fun getResolvedPaths(): ResolvedPaths {
        return ResolvedPaths(
            envFile = envFile,
            secretsDir = secretsDir
        )
    }

    private fun resolveFile(root: File, path: String): File {
        val file = File(path)
        return if (file.isAbsolute) {
            file
        } else {
            File(root, path)
        }
    }
}