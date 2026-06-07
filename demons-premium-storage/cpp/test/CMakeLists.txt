find_package(GTest QUIET)

if(GTEST_FOUND)
    set(TEST_SOURCES
        ApplicationTest.cpp
        StoragePartitionTest.cpp
        ApplicationManagerTest.cpp
    )

    add_executable(demons_premium_storage_tests ${TEST_SOURCES})
    target_link_libraries(demons_premium_storage_tests PRIVATE demons_premium_storage GTest::GTest GTest::Main)
    
    add_test(NAME DemonsPremiumStorageTests COMMAND demons_premium_storage_tests)
else()
    message(WARNING "GTest not found - tests will not be built")
endif()
