package com.group6.accommodation.domain.accommodation.model.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ApiResponseDto {
    private Response response;

    public static class Response {
        private Body body;

        public Body getBody() {
            return body;
        }

        public void setBody(Body body) {
            this.body = body;
        }
    }

    public static class Body {
        private int totalCount;
        private Items items;

        public int getTotalCount() {
            return totalCount;
        }

        public void setTotalCount(int totalCount) {
            this.totalCount = totalCount;
        }

        public Items getItems() {
            return items;
        }

        public void setItems(Items items) {
            this.items = items;
        }
    }

    public static class Items {
        private List<Item> item;

        public List<Item> getItem() {
            return item;
        }

        public void setItem(List<Item> item) {
            this.item = item;
        }
    }

    @Getter
    public static class Item {
        private Long contentid;
        private String title;
        private String addr1;
        private String addr2;
        private String areacode;
        private String sigungucode;
        private String cat3;
        private String firstimage;
        private String firstimage2;
        private Double mapy;
        private Double mapx;
        private Integer mlevel;
        private String tel;
        private int likeCount;
        private Double rating;
    }

}
