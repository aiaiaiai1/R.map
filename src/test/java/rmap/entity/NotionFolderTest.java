package rmap.entity;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import rmap.factory.UserFixture;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static rmap.factory.NotionFolderFixture.DEFAULT;

class NotionFolderTest {

    @Nested
    class 노션_폴더_공개_비공개_상태_설정 {

        @Test
        void 노션_폴더_소유자는_노션_폴더의_공개_상태를_설정_할_수_있다() {
            // given
            NotionFolder notionFolder = DEFAULT.notionFolder();
            User owner = notionFolder.getOwner();
            notionFolder.setPrivateBy(owner);

            // when
            notionFolder.setPublicBy(owner);

            // then
            assertThat(notionFolder.isPublic()).isTrue();
        }

        @Test
        void 노션_폴더_소유자는_노션_폴더의_비공개_상태를_설정_할_수_있다() {
            // given
            NotionFolder notionFolder = DEFAULT.notionFolder();
            User owner = notionFolder.getOwner();

            // when
            notionFolder.setPrivateBy(owner);

            // then
            assertThat(notionFolder.isPrivate()).isTrue();
        }

        @Test
        void 노션_폴더_소유자가_아닌_사용자가_노션_폴더의_공개_상태를_설정_하는_경우_예외가_발생_한다() {
            // given
            NotionFolder notionFolder = DEFAULT.notionFolder();
            User user = UserFixture.DEFAULT_1.user();

            // when, then
            assertThatThrownBy(() -> notionFolder.setPublicBy(user))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        void 노션_폴더_소유자가_아닌_사용자가_노션_폴더의_비공개_상태를_설정_하는_경우_예외가_발생_한다() {
            // given
            NotionFolder notionFolder = DEFAULT.notionFolder();
            User user = UserFixture.DEFAULT_1.user();

            // when, then
            assertThatThrownBy(() -> notionFolder.setPrivateBy(user))
                    .isInstanceOf(IllegalArgumentException.class);
        }

    }

}
