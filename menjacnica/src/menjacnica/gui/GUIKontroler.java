package menjacnica.gui;

import java.awt.EventQueue;
import java.io.File;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.JTextField;

import menjacnica.Menjacnica;
import menjacnica.MenjacnicaInterface;
import menjacnica.Valuta;
import menjacnica.gui.models.MenjacnicaTableModel;

public class GUIKontroler {
	
	public static Menjacnica sistem = new Menjacnica();
	public static MenjacnicaGUI glavniProzor;
	
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					GUIKontroler.glavniProzor = new MenjacnicaGUI();
					GUIKontroler.glavniProzor.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	public static void ugasiAplikaciju(JPanel contentPane) {
		int opcija = JOptionPane.showConfirmDialog(contentPane, "Da li ZAISTA zelite da izadjete iz apliacije",
				"Izlazak", JOptionPane.YES_NO_OPTION);

		if (opcija == JOptionPane.YES_OPTION)
			System.exit(0);
	}
	
	public static void prikaziAboutProzor(JPanel contentPane) {
		JOptionPane.showMessageDialog(contentPane, "Autor: Bojan Tomic, Verzija 1.0", "O programu Menjacnica",
				JOptionPane.INFORMATION_MESSAGE);
	}
	
	public static void sacuvajUFajl(JPanel contentPane) {
		try {
			JFileChooser fc = new JFileChooser();
			int returnVal = fc.showSaveDialog(contentPane);

			if (returnVal == JFileChooser.APPROVE_OPTION) {
				File file = fc.getSelectedFile();

				sistem.sacuvajUFajl(file.getAbsolutePath());
			}
		} catch (Exception e1) {
			JOptionPane.showMessageDialog(contentPane, e1.getMessage(), "Greska", JOptionPane.ERROR_MESSAGE);
		}
	}
	
	public static void ucitajIzFajla(JPanel contentPane, JTable table) {
		try {
			JFileChooser fc = new JFileChooser();
			int returnVal = fc.showOpenDialog(contentPane);

			if (returnVal == JFileChooser.APPROVE_OPTION) {
				File file = fc.getSelectedFile();
				sistem.ucitajIzFajla(file.getAbsolutePath());
				prikaziSveValute(table);
			}
		} catch (Exception e1) {
			JOptionPane.showMessageDialog(contentPane, e1.getMessage(), "Greska", JOptionPane.ERROR_MESSAGE);
		}
	}
	
	public static void prikaziSveValute(JTable table) {
		MenjacnicaTableModel model = (MenjacnicaTableModel) (table.getModel());
		model.staviSveValuteUModel(sistem.vratiKursnuListu());

	}
	
	public static void prikaziDodajKursGUI(JPanel contentPane) {
		DodajKursGUI prozor = new DodajKursGUI();
		prozor.setLocationRelativeTo(contentPane);
		prozor.setVisible(true);
	}
	public static void prikaziObrisiKursGUI(JTable table, JPanel contentPane) {

		if (table.getSelectedRow() != -1) {
			MenjacnicaTableModel model = (MenjacnicaTableModel) (table.getModel());
			ObrisiKursGUI prozor = new ObrisiKursGUI(model.vratiValutu(table.getSelectedRow()));
			prozor.setLocationRelativeTo(contentPane);
			prozor.setVisible(true);
		}
		
	}
	public static void prikaziIzvrsiZamenuGUI(JTable table, JPanel contentPane) {
		if (table.getSelectedRow() != -1) {
			MenjacnicaTableModel model = (MenjacnicaTableModel) (table.getModel());
			IzvrsiZamenuGUI prozor = new IzvrsiZamenuGUI(model.vratiValutu(table.getSelectedRow()));
			prozor.setLocationRelativeTo(contentPane);
			prozor.setVisible(true);
		}
	}
	
	public static void unesiKurs(JPanel contentPane, String naziv, String skraceniNaziv,
			int sifra, double prodajniKurs, double kupovniKurs,
			double srednjiKurs) {
		try {
			Valuta valuta = new Valuta();

			// Punjenje podataka o valuti
			valuta.setNaziv(naziv);
			valuta.setSkraceniNaziv(skraceniNaziv);
			valuta.setSifra(sifra);
			valuta.setProdajni(prodajniKurs);
			valuta.setKupovni(kupovniKurs);
			valuta.setSrednji(srednjiKurs);
			
			// Dodavanje valute u kursnu listu
			sistem.dodajValutu(valuta);

			// Osvezavanje glavnog prozora
			glavniProzor.prikaziSveValute();
			
			//Zatvaranje DodajValutuGUI prozora
			
		} catch (Exception e1) {
			JOptionPane.showMessageDialog(contentPane, e1.getMessage(),
					"Greska", JOptionPane.ERROR_MESSAGE);
		}
	}
	
	public static double izvrsiZamenu(JPanel contentPane, double iznos, Valuta valuta, boolean prodaja) {
		try {
			double konacniIznos = sistem.izvrsiTransakciju(valuta, prodaja, iznos);

			return konacniIznos;
		} catch (Exception e1) {
			JOptionPane.showMessageDialog(contentPane, e1.getMessage(), "Greska", JOptionPane.ERROR_MESSAGE);
		}
		
		return -1;
	}
	
	public static void obrisiValutu(Valuta valuta, JPanel contentPane) {
		try{
			sistem.obrisiValutu(valuta);
			
			glavniProzor.prikaziSveValute();
		} catch (Exception e1) {
			JOptionPane.showMessageDialog(contentPane, e1.getMessage(),
					"Greska", JOptionPane.ERROR_MESSAGE);
		}
	}
	
	

}
