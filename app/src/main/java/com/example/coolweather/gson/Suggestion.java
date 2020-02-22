package com.example.coolweather.gson;

import com.google.gson.annotations.SerializedName;

public class Suggestion {


    @SerializedName("comf")
    public Comfort comfort;//舒适度指数1

    public class Comfort{

        @SerializedName("txt")
        public String info;

    }

    @SerializedName("cw")
    public CarWash carWash;//洗车指数1

    public class CarWash{

        @SerializedName("txt")
        public String info;
    }

    public Drsg drsg;//穿衣指数

    public class Drsg{

        @SerializedName("txt")
        public String info;
    }

    public Flu flu;//感冒指数

    public class Flu{

        @SerializedName("txt")
        public String info;
    }

    public Sport sport;//运动指数1

    public class Sport{

        @SerializedName("txt")
        public String info;
    }

    public Trav trav;//旅游指数

    public class Trav{

        @SerializedName("txt")
        public String info;
    }

    public Uv uv;//紫外线指数

    public class Uv{

        @SerializedName("txt")
        public String info;
    }
}

