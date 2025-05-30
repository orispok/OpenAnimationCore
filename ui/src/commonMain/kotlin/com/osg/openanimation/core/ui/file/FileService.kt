package com.osg.openanimation.core.ui.file

interface FileService{
    fun saveFile(byteArray: ByteArray, fileName: String)
}

expect val fileService: FileService