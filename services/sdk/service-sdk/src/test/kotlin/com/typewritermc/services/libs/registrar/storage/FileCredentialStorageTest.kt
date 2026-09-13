package com.typewritermc.services.libs.registrar.storage

import com.typewritermc.services.libs.registrar.CredentialLoadResult
import com.typewritermc.services.libs.registrar.CredentialStorageError
import com.typewritermc.services.libs.registrar.CredentialStoreResult
import com.typewritermc.services.libs.registrar.IdentityCredentials
import com.typewritermc.services.libs.registrar.RedactedSecret
import com.typewritermc.services.libs.registrar.ServiceIdentity
import com.typewritermc.services.libs.registrar.ServiceRole
import de.infix.testBalloon.framework.core.testSuite
import io.kotest.matchers.shouldBe
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.runTest
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.Serializable
import kotlinx.serialization.cbor.Cbor
import java.nio.file.Files

private fun credentials(id: String = "service-id") =
    IdentityCredentials(
        ServiceIdentity(
            id,
            "Service Name",
            "service-user",
            ServiceRole.Custom("custom_role", "7.8.9"),
        ),
        RedactedSecret.AppPassword("private-password"),
    )

@OptIn(ExperimentalSerializationApi::class)
val FileCredentialStorageTest by testSuite {
    test("missing file returns Missing") {
        val directory = Files.createTempDirectory("registrar-storage")
        try {
            runTest {
                FileCredentialStorage(directory.resolve("identity.cbor"), dispatcher = StandardTestDispatcher(testScheduler))
                    .load() shouldBe CredentialLoadResult.Missing
            }
        } finally {
            directory.toFile().deleteRecursively()
        }
    }

    test("round trips every identity field and issued service role") {
        val directory = Files.createTempDirectory("registrar-storage")
        try {
            runTest {
                val storage =
                    FileCredentialStorage(
                        directory.resolve("identity.cbor"),
                        dispatcher = StandardTestDispatcher(testScheduler),
                    )
                storage.store(credentials()) shouldBe CredentialStoreResult.Success
                val loaded = storage.load() as CredentialLoadResult.Loaded
                loaded.credentials.identity.serviceId shouldBe "service-id"
                loaded.credentials.identity.displayName shouldBe "Service Name"
                loaded.credentials.identity.username shouldBe "service-user"
                loaded.credentials.identity.role shouldBe ServiceRole.Custom("custom_role", "7.8.9")
                loaded.credentials.revealAppPassword() shouldBe "private-password"
            }
        } finally {
            directory.toFile().deleteRecursively()
        }
    }

    test("replaces an existing identity") {
        val directory = Files.createTempDirectory("registrar-storage")
        try {
            runTest {
                val storage =
                    FileCredentialStorage(
                        directory.resolve("identity.cbor"),
                        dispatcher = StandardTestDispatcher(testScheduler),
                    )
                storage.store(credentials("first")) shouldBe CredentialStoreResult.Success
                storage.store(credentials("second")) shouldBe CredentialStoreResult.Success
                val loaded = storage.load() as CredentialLoadResult.Loaded
                loaded.credentials.identity.serviceId shouldBe "second"
                Files.list(directory).use { it.count() } shouldBe 1L
            }
        } finally {
            directory.toFile().deleteRecursively()
        }
    }

    test("malformed file is corrupt") {
        val directory = Files.createTempDirectory("registrar-storage")
        try {
            val path = directory.resolve("identity.cbor")
            Files.writeString(path, "not-cbor")
            runTest {
                val result = FileCredentialStorage(path, dispatcher = StandardTestDispatcher(testScheduler)).load()
                ((result as CredentialLoadResult.Failure).error is CredentialStorageError.Corrupt) shouldBe true
            }
        } finally {
            directory.toFile().deleteRecursively()
        }
    }

    test("unknown version is explicit") {
        val directory = Files.createTempDirectory("registrar-storage")
        try {
            val path = directory.resolve("identity.cbor")
            Files.write(
                path,
                Cbor.Default.encodeToByteArray(
                    StoredCredentialFixture.serializer(),
                    StoredCredentialFixture(
                        version = 3,
                        serviceId = "id",
                        displayName = "name",
                        username = "user",
                        issuedServiceRole = StoredServiceRoleFixture("host", "1.0.0"),
                        token = "token",
                    ),
                ),
            )
            runTest {
                val result = FileCredentialStorage(path, dispatcher = StandardTestDispatcher(testScheduler)).load()
                (result as CredentialLoadResult.Failure).error shouldBe CredentialStorageError.UnsupportedVersion(3)
            }
        } finally {
            directory.toFile().deleteRecursively()
        }
    }

    test("oversized file is corrupt without reading it") {
        val directory = Files.createTempDirectory("registrar-storage")
        try {
            val path = directory.resolve("identity.cbor")
            Files.write(path, ByteArray(33) { 1 })
            runTest {
                val result =
                    FileCredentialStorage(
                        path,
                        maximumBytes = 32,
                        dispatcher = StandardTestDispatcher(testScheduler),
                    ).load()
                ((result as CredentialLoadResult.Failure).error is CredentialStorageError.Corrupt) shouldBe true
            }
        } finally {
            directory.toFile().deleteRecursively()
        }
    }

    test("directories and symbolic links are corrupt inputs") {
        val directory = Files.createTempDirectory("registrar-storage")
        try {
            runTest {
                val result = FileCredentialStorage(directory, dispatcher = StandardTestDispatcher(testScheduler)).load()
                ((result as CredentialLoadResult.Failure).error is CredentialStorageError.Corrupt) shouldBe true
            }
        } finally {
            directory.toFile().deleteRecursively()
        }
    }

    test("credentials and stored diagnostics remain redacted") {
        val value = credentials()
        value.toString().contains("private-password") shouldBe false
    }
}

@Serializable
private data class StoredCredentialFixture(
    val version: Int,
    val serviceId: String,
    val displayName: String,
    val username: String,
    val issuedServiceRole: StoredServiceRoleFixture,
    val token: String,
)

@Serializable
private data class StoredServiceRoleFixture(
    val type: String,
    val version: String,
    val name: String? = null,
)
