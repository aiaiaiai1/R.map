package rmap.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class NotionFolder {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false, updatable = false)
    private User owner;

    @Column(length = 100, nullable = false)
    private String name;

    @Column(nullable = false)
    private boolean isPrivate;

    public NotionFolder(User creator, String notionFolderName) {
        this(creator, notionFolderName, false);
    }

    public NotionFolder(User creator, String notionFolderName, boolean isPrivate) {
        this.owner = creator;
        this.name = notionFolderName;
        this.isPrivate = isPrivate;
    }

    public void changeName(String name) {
        this.name = name;
    }

    public boolean isOwner(User user) {
        return this.owner.equals(user);
    }

    public boolean isAccessibleBy(User user) {
        return this.owner.equals(user) || this.isPublic();
    }

    public void setPublic(User owner) {
        if (!this.owner.equals(owner)) {
            throw new IllegalArgumentException();
        }
        this.isPrivate = false;
    }

    public void setPrivateBy(User owner) {
        if (!this.owner.equals(owner)) {
            throw new IllegalArgumentException();
        }
        this.isPrivate = true;
    }

    public boolean isPrivate() {
        return this.isPrivate;
    }

    public boolean isPublic() {
        return !this.isPrivate;
    }


}
