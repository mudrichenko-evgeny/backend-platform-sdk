package com.mudrichenkoevgeny.backend.core.common.validation

import com.mudrichenkoevgeny.backend.core.common.error.model.AppError

class ValidationException(val error: AppError) : RuntimeException()