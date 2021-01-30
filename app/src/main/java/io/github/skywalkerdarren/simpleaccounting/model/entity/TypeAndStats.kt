package io.github.skywalkerdarren.simpleaccounting.model.entity

import java.io.Serializable

//data class TypeAndStats(
//        val type: Type,
//        val typeStats: TypeStats
//)

data class TypeAndStats constructor(
        val type: Type,
        val typeStats: TypeStats): Serializable