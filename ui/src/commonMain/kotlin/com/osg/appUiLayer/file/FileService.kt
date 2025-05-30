package com.osg.appUiLayer.file

interface FileService{
    fun saveFile(byteArray: ByteArray, fileName: String)
}

expect val fileService: FileService