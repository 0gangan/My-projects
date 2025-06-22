package lzg.example.formalSecurityTest.domain;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity
public class BookCategory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_id")
    @JsonBackReference
    private BookCategory parent;

    @OneToMany(mappedBy = "parent", cascade = CascadeType.ALL)
    @JsonManagedReference
    private List<BookCategory> children = new ArrayList<>();

    @Transient
    private Long parentId; // 只用于接收请求参数

    // --- getter/setter ---

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public BookCategory getParent() { return parent; }
    public void setParent(BookCategory parent) { this.parent = parent; }

    public List<BookCategory> getChildren() { return children; }
    public void setChildren(List<BookCategory> children) { this.children = children; }
    @JsonProperty("parentId")  // 让它序列化为 parentId
    public Long getParentId() {
        return parent != null ? parent.getId() : null;
    }

    public void setParentId(Long parentId) { this.parentId = parentId; }
}
