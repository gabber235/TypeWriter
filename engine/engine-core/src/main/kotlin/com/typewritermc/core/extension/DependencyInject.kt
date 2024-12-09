package com.typewritermc.core.extension

import com.typewritermc.core.extension.annotations.Inject
import com.typewritermc.core.extension.annotations.Parameter
import com.typewritermc.core.utils.Reloadable
import com.typewritermc.loader.DependencyInjectionClassInfo
import com.typewritermc.loader.DependencyInjectionMethodInfo
import com.typewritermc.loader.ExtensionLoader
import com.typewritermc.loader.SerializableType
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import org.koin.core.context.loadKoinModules
import org.koin.core.context.unloadKoinModules
import org.koin.core.definition.KoinDefinition
import org.koin.core.module.Module
import org.koin.core.parameter.ParametersHolder
import org.koin.core.qualifier.named
import org.koin.core.scope.Scope
import org.koin.dsl.binds
import org.koin.dsl.module
import kotlin.reflect.KClass
import kotlin.reflect.KParameter
import kotlin.reflect.full.findAnnotations
import kotlin.reflect.full.primaryConstructor
import kotlin.reflect.full.superclasses
import kotlin.reflect.jvm.kotlinFunction

class DependencyInject : KoinComponent, Reloadable {
    private val extensionLoader: ExtensionLoader by inject()

    private var module: Module? = null

    override suspend fun load() {
        module = module {
            extensionLoader.extensions.flatMap { it.dependencyInjections }
                .map { it to extensionLoader.loadClass(it.className) }
                .forEach { (blueprint, clazz) ->
                    when (blueprint) {
                        is DependencyInjectionClassInfo ->
                            when (blueprint.type) {
                                SerializableType.SINGLETON -> {
                                    if (blueprint.name != null) {
                                        single(named(blueprint.name!!)) {
                                            createClass(it, clazz.kotlin)
                                        } bindToSuperclasses clazz.kotlin
                                    } else {
                                        single {
                                            createClass(it, clazz.kotlin)
                                        } bindToSuperclasses clazz.kotlin
                                    }
                                }

                                SerializableType.FACTORY -> {
                                    if (blueprint.name != null) {
                                        factory(named(blueprint.name!!)) {
                                            createClass(it, clazz.kotlin)
                                        } bindToSuperclasses clazz.kotlin
                                    } else {
                                        factory {
                                            createClass(it, clazz.kotlin)
                                        } bindToSuperclasses clazz.kotlin
                                    }
                                }
                            }

                        is DependencyInjectionMethodInfo ->
                            when (blueprint.type) {
                                SerializableType.SINGLETON -> {
                                    if (blueprint.name != null) {
                                        single(named(blueprint.name!!)) {
                                            createMethod(it, clazz, blueprint.methodName)
                                        }.bindToReturnType(clazz, blueprint.methodName)
                                    } else {
                                        single {
                                            createMethod(it, clazz, blueprint.methodName)
                                        }.bindToReturnType(clazz, blueprint.methodName)
                                    }
                                }

                                SerializableType.FACTORY -> {
                                    if (blueprint.name != null) {
                                        factory(named(blueprint.name!!)) {
                                            createMethod(it, clazz, blueprint.methodName)
                                        }.bindToReturnType(clazz, blueprint.methodName)
                                    } else {
                                        factory {
                                            createMethod(it, clazz, blueprint.methodName)
                                        }.bindToReturnType(clazz, blueprint.methodName)
                                    }
                                }
                            }
                    }
                }
        }.also {
            loadKoinModules(it)
        }

    }

    private fun Scope.createClass(param: ParametersHolder, klass: KClass<*>): Any {
        klass.objectInstance?.let { return it }

        val primaryConstructor = klass.primaryConstructor
            ?: klass.constructors.firstOrNull()
            ?: throw IllegalArgumentException("No primary constructor found for $klass")

        val parameters = primaryConstructor.parameters.generateParameters(this, param)
        return primaryConstructor.call(*parameters)
    }

    private fun Scope.createMethod(param: ParametersHolder, clazz: Class<*>, methodName: String): Any? {
        val method = clazz.declaredMethods.firstOrNull { it.name == methodName }?.kotlinFunction
            ?: throw NoSuchMethodException("No method found with name $methodName in ${clazz.name}")

        val parameters = method.parameters.generateParameters(this, param)
        return method.call(*parameters)
    }

    private fun List<KParameter>.generateParameters(scope: Scope, param: ParametersHolder): Array<Any?> {
        return map { parameter ->
            val paramAnnotation = parameter.findAnnotations(Parameter::class).firstOrNull()
            val type = parameter.type.classifier as? KClass<*>
                ?: throw IllegalArgumentException("Parameter ${parameter.name} is not a class")

            if (paramAnnotation != null) {
                return@map param.getOrNull<Any?>(type)
            }

            val injectAnnotation = parameter.findAnnotations(Inject::class).firstOrNull()
            if (injectAnnotation != null) {
                return@map scope.get(type, named(injectAnnotation.name))
            }

            return@map scope.get(type)
        }
            .toTypedArray()
    }

    private infix fun KoinDefinition<*>.bindToSuperclasses(clazz: KClass<*>) {
        binds((clazz.superclasses + clazz).toTypedArray())
    }

    private fun KoinDefinition<*>.bindToReturnType(clazz: Class<*>, methodName: String) {
        val method = clazz.declaredMethods.firstOrNull { it.name == methodName }?.kotlinFunction ?: return
        val returnType = method.returnType.classifier as? KClass<*> ?: return
        binds(arrayOf(returnType))
    }

    override suspend fun unload() {
        module?.let { unloadKoinModules(it) }
        module = null
    }
}