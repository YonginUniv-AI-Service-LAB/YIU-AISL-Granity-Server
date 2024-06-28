package yiu.aisl.granity.domain.state;

import lombok.Getter;

@Getter
public enum RoleCategory {
    관리자(0),
    학생(1),
    조교및교수(2);

    private final int value;

    RoleCategory(Integer value) {
        this.value = value;
    }

    public static RoleCategory fromInt(int value) {
        for(RoleCategory role : RoleCategory.values()) {
            if(role.getValue() == value) {
                return role;
            }
        }
        throw new IllegalArgumentException("유효하지 않은 카테고리 값: " +value);
    }
}
