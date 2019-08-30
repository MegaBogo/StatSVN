package org.jfree.chart.axis;

import java.awt.Image;

import org.jfree.ui.TextAnchor;
import org.jfree.util.ObjectUtilities;

public class CategoryImageTick extends CategoryTick {

	private Image image;
	
	public CategoryImageTick(Comparable category, Image image, TextAnchor rotationAnchor, double angle) {
		super(category, TextAnchor.BASELINE_LEFT, rotationAnchor, angle);
		
		this.image = image;
	}
	
	public Image getImage() {
		return this.image;
	}
	
    /**
     * Tests this image tick for equality with an arbitrary object.
     *
     * @param obj  the object (<code>null</code> permitted).
     *
     * @return A boolean.
     */
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
        if (obj instanceof CategoryImageTick && super.equals(obj)) {
        	CategoryImageTick that = (CategoryImageTick) obj;
            if (!ObjectUtilities.equal(this.category, that.category)) {
                return false;
            }
            if (!ObjectUtilities.equal(this.image, that.image)) {
                return false;
            }
            return true;
        }
        return false;
	}
	
    /**
     * Returns a hash code for this object.
     *
     * @return A hash code.
     */
    public int hashCode() {
        int result = 41;
        result = 37 * result + this.category.hashCode();
        result = 37 * result + this.image.hashCode();
        return result;
    }
}
