package me.crosswall.lib.coverflow;

import android.support.v4.view.ViewPager;

import me.crosswall.lib.coverflow.core.CoverTransformer;

public class CoverFlow {

    //  public static final float DEFAULT_SCALE_VALUE  = 0.3f;

    //  public static final float DEFAULT_PAGER_MARGIN = 0f;

    //   public static final float DEFAULT_SPACE_SIZE   = 0f;

    private final ViewPager viewPager;
    private final float scaleValue;
    private final float pagerMargin;
    private final float spaceSize;
    private final float rotationY;

    public CoverFlow(Builder builder){
        if(null==builder){
            throw new IllegalArgumentException("A non-null CoverFlow.Builde must be provided");
        }
        this.viewPager = builder.viewPager;
        this.scaleValue  = builder.scaleValue;
        this.pagerMargin = builder.pagerMargin;
        this.spaceSize   = builder.spaceSize;
        this.rotationY   = builder.rotationY;

        if(this.viewPager != null){
            this.viewPager.setPageTransformer(false,
                    new CoverTransformer(this.scaleValue,this.pagerMargin,this.spaceSize,this.rotationY));
        }
    }

    public static class Builder {
        private ViewPager viewPager;
        private float scaleValue;
        private float pagerMargin;
        private float spaceSize;
        private float rotationY;

        public Builder with(ViewPager viewPager) {
            this.viewPager = viewPager;
            return this;
        }

        public Builder scale(float scaleValue) {
            this.scaleValue = scaleValue;
            return this;
        }

        public Builder pagerMargin(float pagerMargin) {
            this.pagerMargin = pagerMargin;
            return this;
        }

        public Builder spaceSize(float spaceSize) {
            this.spaceSize = spaceSize;
            return this;
        }

        public Builder rotationY(float rotationY){
            this.rotationY = rotationY;
            return this;
        }

        public CoverFlow build() {
            return new CoverFlow(this);

        }
    }

}
