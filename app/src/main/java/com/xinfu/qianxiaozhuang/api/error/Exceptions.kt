package com.xiang.one.network.error

import java.lang.Exception

open class CustomException : Exception()

class ConnectFailedAlertDialogException : CustomException()

class TokenExpiredException : CustomException()

class LoginTimeOutException: CustomException()

class ServerException: CustomException()