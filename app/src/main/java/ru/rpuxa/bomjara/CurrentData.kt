package ru.rpuxa.bomjara

import ru.rpuxa.bomjara.api.data.Data
import ru.rpuxa.bomjara.api.data.MutableData
import ru.rpuxa.bomjara.impl.data.DataImpl

object CurrentData : Data by DataImpl

object CurrentMutableData : MutableData by DataImpl