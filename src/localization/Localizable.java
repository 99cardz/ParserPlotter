package localization;

public interface Localizable {
	// registers this in the given Localizer linking it to the keys translations;
	public void setTranslations(String key, Localizer localizer);
	public void update();
}
