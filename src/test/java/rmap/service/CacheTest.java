package rmap.service;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

class CacheTest {

    @Test
    void 캐시에_저장한_데이터는_일정_시간이_지나면_만료된다() throws InterruptedException {
        // given
        Cache cache = new Cache(500);

        // when
        cache.put("test", "test");
        assertThat(cache.containsKey("test")).isTrue();
        Thread.sleep(600);

        // then
        assertThat(cache.containsKey("test")).isFalse();
    }

    @Test
    void 캐시에_저장한_데이터가_중간에_수정되면_캐시_타임도_수정된다() throws InterruptedException {
        // given
        Cache cache = new Cache(500);

        // when
        cache.put("test", "test");
        Thread.sleep(300);
        cache.put("test", "test");
        Thread.sleep(300);
        assertThat(cache.containsKey("test")).isTrue();
        Thread.sleep(300);

        // then
        assertThat(cache.containsKey("test")).isFalse();
    }

}
