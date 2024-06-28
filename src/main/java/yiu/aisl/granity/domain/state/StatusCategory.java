package yiu.aisl.granity.domain.state;

import lombok.Getter;

@Getter
public enum StatusCategory {
    휴학(0),
    재학(1);

    private final int value;

    StatusCategory(Integer value) {
        this.value = value;
    }

    public static StatusCategory fromInt(int value) {
        for(StatusCategory role : StatusCategory.values()) {
            if(role.getValue() == value) {
                return role;
            }
        }
        throw new IllegalArgumentException("유효하지 않은 카테고리 값: " +value);
    }
}
