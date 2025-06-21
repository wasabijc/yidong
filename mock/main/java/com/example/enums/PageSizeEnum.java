package com.example.enums;

public enum PageSizeEnum
{
    SIZE15(15), SIZE20(20), SIZE30(30), SIZE40(40), SIZE(50);
    int size;

    PageSizeEnum(int size)
    {
        this.size = size;
    }

    public int getSize()
    {
        return size;
    }
}
