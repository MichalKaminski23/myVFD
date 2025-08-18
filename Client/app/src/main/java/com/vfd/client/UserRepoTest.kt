package com.vfd.client

class UserRepoTest(
    private val userApiTest: UserApiTest = NetworkModuleTest.api
) {
    suspend fun fetchAllUsers(): List<UserTestDto> =
        userApiTest.getUsers().items
}