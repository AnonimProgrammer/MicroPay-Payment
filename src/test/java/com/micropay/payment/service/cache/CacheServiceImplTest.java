package com.micropay.payment.service.cache;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;

import java.util.function.Supplier;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class CacheServiceImplTest {

    private CacheManager cacheManager;
    private ObjectMapper objectMapper;
    private CacheServiceImpl cacheService;
    private Cache cache;

    @BeforeEach
    void setUp() {
        cacheManager = mock(CacheManager.class);
        objectMapper = mock(ObjectMapper.class);
        cacheService = new CacheServiceImpl(cacheManager, objectMapper);
        cache = mock(Cache.class);
    }

    @Test
    void getOrPut_ShouldReturnValueFromSupplier_WhenCacheNotFound() {
        when(cacheManager.getCache("cache")).thenReturn(null);

        String result = cacheService
                .getOrPut("cache", "key", new TypeReference<String>() {}, () -> "dbValue");

        assertEquals("dbValue", result);
    }

    @Test
    void getOrPut_ShouldReturnCachedValue_WhenCacheHit() {
        when(cacheManager.getCache("cache")).thenReturn(cache);
        when(cache.get("key", Object.class)).thenReturn("cached");
        when(objectMapper.convertValue(eq("cached"), any(TypeReference.class))).thenReturn("converted");

        Supplier<String> supplier = mock(Supplier.class);

        String result = cacheService
                .getOrPut("cache", "key", new TypeReference<String>() {}, supplier);

        assertEquals("converted", result);
        verify(supplier, never()).get();
        verify(cache, times(1)).get("key", Object.class);
        verify(objectMapper, times(1)).convertValue(eq("cached"), any(TypeReference.class));
    }

    @Test
    void getOrPut_ShouldCallSupplier_WhenCacheMiss() {
        when(cacheManager.getCache("cache")).thenReturn(cache);
        when(cache.get("key", Object.class)).thenReturn(null);

        Supplier<String> supplier = () -> "dbValue";

        String result = cacheService.getOrPut("cache", "key", new TypeReference<String>() {}, supplier);

        assertEquals("dbValue", result);
        verify(cache, times(1)).put("key", "dbValue");
    }

    @Test
    void getOrPut_ShouldCallSupplier_WhenDeserializationFails() {
        when(cacheManager.getCache("cache")).thenReturn(cache);
        when(cache.get("key", Object.class)).thenReturn("cached");
        when(objectMapper.convertValue(eq("cached"), any(TypeReference.class)))
                .thenThrow(new RuntimeException("fail"));

        Supplier<String> supplier = mock(Supplier.class);
        when(supplier.get()).thenReturn("dbValue");

        String result = cacheService
                .getOrPut("cache", "key", new TypeReference<String>() {}, supplier);

        assertEquals("dbValue", result);
        verify(cache, times(1)).put("key", "dbValue");
        verify(supplier, times(1)).get();
    }

    @Test
    void evictAll_ShouldClearCache_WhenCacheExists() {
        when(cacheManager.getCache("cache")).thenReturn(cache);

        cacheService.evictAll("cache");

        verify(cache, times(1)).clear();
    }

    @Test
    void evictAll_ShouldLogWarning_WhenCacheNotFound() {
        when(cacheManager.getCache("cache")).thenReturn(null);

        cacheService.evictAll("cache");

        verify(cacheManager, times(1)).getCache("cache");
    }
}
