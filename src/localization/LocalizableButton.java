package localization;

import javax.swing.JButton;

public class LocalizableButton extends JButton implements Localizable {
	private Localizer module;
	private String key;
	
	public LocalizableButton(String key, Localizer module) {
		setTranslations(key, module);
	}

	@Override
	public void setTranslations(String key, Localizer module) {
		this.module = module;
		this.key = key;
		this.module.register(this);
	}
	@Override
	public void update() {
		this.setText(module.getTranslation(key));
		System.out.println();
		this.revalidate();
	}
}
