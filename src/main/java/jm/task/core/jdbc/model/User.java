package jm.task.core.jdbc.model;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Objects;

@Table
public class User {
    @Id
    private Long id;

    @Column
    private String name;

    @Column
    private String lastName;

    @Column
    private Byte age;

    public User() {

    }

    public User(String name, String lastName, Byte age) {
        this.name = name;
        this.lastName = lastName;
        this.age = age;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public Byte getAge() {
        return age;
    }

    public void setAge(Byte age) {
        this.age = age;
    }


    // Нарушение контрактов ради совместимости с orm вроде как
    // Доразабраться с этим вопросом
    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || this.getClass() != o.getClass()) {
            return false;
        }

        User other = (User) o;
        if (this.id != null && this.id != 0L) {
            return this.id.equals(other.id);
        }
        return false;
    }

    @Override
    public int hashCode() {
        return this.id != null ? Long.hashCode(this.id) : 0;
    }
}
