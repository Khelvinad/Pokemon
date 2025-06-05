import entity.Player;
import game.GamePanel;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import javax.swing.*;
import logic.*;

class LoadButtonAction extends MenuButtonAction {

    private JPanel selectedSlotPanel = null;
    private Map<String, String> saveInfoForSelectedSlot = null;
    private JButton actualLoadButton;

    public LoadButtonAction(Frame frameApp) {
        super(frameApp);
    }

    @Override
    public void execute() {
        frame.getContentPane().removeAll();
        JPanel loadGamePanel = createBackgroundPanel("src/Asset/Bg2.jpg");
        if (loadGamePanel == null) {
            loadGamePanel = new JPanel();
            loadGamePanel.setBackground(new Color(25, 25, 80));
        }
        loadGamePanel.setLayout(new BorderLayout(10, 10));
        loadGamePanel.setBorder(BorderFactory.createEmptyBorder(30, 50, 30, 50));

        JLabel titleLabel = new JLabel("Load Game", SwingConstants.CENTER);
        titleLabel.setFont(customFont.deriveFont(Font.BOLD, 40f));
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setBorder(BorderFactory.createEmptyBorder(0,0,20,0));
        loadGamePanel.add(titleLabel, BorderLayout.NORTH);

        JPanel slotsPanel = new JPanel();
        slotsPanel.setOpaque(false);
        slotsPanel.setLayout(new BoxLayout(slotsPanel, BoxLayout.Y_AXIS));

        Map<String, String> saveInfoSlot1 = DataHandler.peekSaveFileInfo();
        JPanel slotPanel1 = createSlotPanel(1, saveInfoSlot1);
        slotsPanel.add(slotPanel1);
        slotsPanel.add(Box.createRigidArea(new Dimension(0, 15)));

        slotsPanel.add(createSlotPanel(2, null));
        slotsPanel.add(Box.createRigidArea(new Dimension(0, 15)));
        slotsPanel.add(createSlotPanel(3, null));
        slotsPanel.add(Box.createRigidArea(new Dimension(0, 15)));
        slotsPanel.add(createSlotPanel(4, null));

        JPanel centerWrap = new JPanel(new GridBagLayout());
        centerWrap.setOpaque(false);
        centerWrap.add(slotsPanel);
        loadGamePanel.add(centerWrap, BorderLayout.CENTER);

        JPanel bottomButtonsPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 30, 10));
        bottomButtonsPanel.setOpaque(false);
        bottomButtonsPanel.setBorder(BorderFactory.createEmptyBorder(20,0,0,0));

        actualLoadButton = new JButton("LOAD");
        styleMenuButton(actualLoadButton, new Color(80, 130, 70));

        JButton backButton = createBackButton();
        styleMenuButton(backButton, new Color(180, 100, 80));

        bottomButtonsPanel.add(backButton);
        bottomButtonsPanel.add(actualLoadButton);

        loadGamePanel.add(bottomButtonsPanel, BorderLayout.SOUTH);

        if (saveInfoSlot1 != null) {
            selectSlot(slotPanel1, saveInfoSlot1);
        } else {
            selectedSlotPanel = null;
            saveInfoForSelectedSlot = null;
        }
        actualLoadButton.setEnabled(saveInfoForSelectedSlot != null);

        actualLoadButton.addActionListener(e -> {
            if (saveInfoForSelectedSlot != null) {
                loadTheGame();
            } else {
                JOptionPane.showMessageDialog(frame, "No save game selected or available to load.", "Load Error", JOptionPane.WARNING_MESSAGE);
            }
        });

        frame.setContentPane(loadGamePanel);
        frame.revalidate();
        frame.repaint();
    }

    private void selectSlot(JPanel slotPanel, Map<String, String> info) {
        if (selectedSlotPanel != null) {
            selectedSlotPanel.setBorder(BorderFactory.createLineBorder(new Color(70, 50, 30), 3));
            selectedSlotPanel.setBackground(new Color(50,50,70));
        }
        selectedSlotPanel = slotPanel;
        saveInfoForSelectedSlot = info;

        if (selectedSlotPanel != null && info != null) {
            selectedSlotPanel.setBorder(BorderFactory.createLineBorder(new Color(200, 180, 100), 3));
            selectedSlotPanel.setBackground(new Color(70,70,90));
        }
        if (actualLoadButton != null) {
            actualLoadButton.setEnabled(saveInfoForSelectedSlot != null);
        }
    }

    private JPanel createSlotPanel(int slotNumber, Map<String, String> info) {
        JPanel slotPanel = new JPanel();
        slotPanel.setLayout(new GridBagLayout());
        slotPanel.setPreferredSize(new Dimension(350, 60));
        slotPanel.setBackground(new Color(50, 50, 70));
        slotPanel.setBorder(BorderFactory.createLineBorder(new Color(70, 50, 30), 3));

        JLabel textLabel = new JLabel();
        textLabel.setFont(customFont.deriveFont(Font.BOLD, 18f));
        textLabel.setForeground(Color.LIGHT_GRAY);

        if (info != null && slotNumber == 1) {
            String playerName = info.getOrDefault("PLAYER_NAME", "Save Data");
            String timestamp = info.getOrDefault("SAVE_TIMESTAMP", "");
            textLabel.setText(playerName + (timestamp.isEmpty() ? "" : " - " + timestamp.substring(0, Math.min(timestamp.length(), 10))));
            slotPanel.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    selectSlot(slotPanel, info);
                }
                @Override
                public void mouseEntered(MouseEvent e) {
                    if (info != null) {
                        slotPanel.setBackground(new Color(65, 65, 85));
                    }
                }
                @Override
                public void mouseExited(MouseEvent e) {
                    if (selectedSlotPanel == slotPanel && info != null) {
                        slotPanel.setBackground(new Color(70,70,90));
                    } else {
                        slotPanel.setBackground(new Color(50, 50, 70));
                    }
                }
            });

        } else {
            textLabel.setText("EMPTY");
            textLabel.setForeground(new Color(80, 80, 80));
        }
        slotPanel.add(textLabel);
        return slotPanel;
    }

    private void styleMenuButton(JButton button, Color bgColor) {
        button.setFont(customFont.deriveFont(Font.BOLD, 20f));
        button.setBackground(bgColor);
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(Color.BLACK, 2),
            BorderFactory.createLineBorder(bgColor.brighter(), 2)
        ));
        button.setPreferredSize(new Dimension(140, 50));
    }

    private void loadTheGame() {
        frame.getContentPane().removeAll();
        Map<String, String> loadedData = DataHandler.loadGameData();

        if (loadedData == null) {
            JOptionPane.showMessageDialog(frame, "Failed to load game data or no save file found.", "Load Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        try {
            GamePanel gamePanel = new GamePanel();
            Player loadedPlayer = gamePanel.player;

            String playerName = loadedData.get("PLAYER_NAME");
            if (playerName == null) throw new NullPointerException("PLAYER_NAME not found.");
            loadedPlayer.setPlayerName(playerName);

            loadedPlayer.worldX = Integer.parseInt(loadedData.getOrDefault("PLAYER_WORLD_X", String.valueOf(loadedPlayer.worldX)));
            loadedPlayer.worldY = Integer.parseInt(loadedData.getOrDefault("PLAYER_WORLD_Y", String.valueOf(loadedPlayer.worldY)));

            String mapId = loadedData.getOrDefault("CURRENT_MAP_ID", "map01.txt");

            Inventory playerInventory = loadedPlayer.getInventory();
            if (playerInventory.getPokemons() != null) {
                playerInventory.getPokemons().clear();
            }

            int partySize = Integer.parseInt(loadedData.getOrDefault("PARTY_SIZE", "0"));
            List<logic.Pokemon> loadedPartyForActivePokemonReference = new ArrayList<>();

            for (int i = 0; i < partySize; i++) {
                String prefix = "POKEMON_" + (i + 1) + "_";
                String pName = loadedData.get(prefix + "NAME");
                if (pName == null) continue;

                logic.Pokemon pokemon = Pokedex.getPokemonData(pName);
                if (pokemon == null) {
                    System.err.println("Warning: Pokedex data not found for " + pName);
                    continue;
                }

                pokemon.setHealth(Integer.parseInt(loadedData.getOrDefault(prefix + "CURRENT_HP", String.valueOf(pokemon.getMaxHealth()))));
                if(loadedData.containsKey(prefix + "MAX_HP")) pokemon.setHealth(Math.min(pokemon.getHealth(), Integer.parseInt(loadedData.get(prefix + "MAX_HP"))));

                playerInventory.addPokemon(pokemon);
                loadedPartyForActivePokemonReference.add(pokemon);
            }

            int activePokemonIndex = Integer.parseInt(loadedData.getOrDefault("ACTIVE_POKEMON_INDEX", "-1"));
            if (activePokemonIndex >= 0 && activePokemonIndex < loadedPartyForActivePokemonReference.size()) {
                loadedPlayer.setActivePokemon(loadedPartyForActivePokemonReference.get(activePokemonIndex));
            } else if (!loadedPartyForActivePokemonReference.isEmpty()) {
                loadedPlayer.setActivePokemon(loadedPartyForActivePokemonReference.get(0));
            }

            for (Map.Entry<String, String> entry : loadedData.entrySet()) {
                if (entry.getKey().startsWith("INVENTORY_ITEM_")) {
                    String itemNameFromFile = entry.getKey().substring("INVENTORY_ITEM_".length());
                    String itemName = itemNameFromFile.replace("_", " ");
                    int quantity = Integer.parseInt(entry.getValue());

                    Items itemToAdd = null;
                    if (itemName.equalsIgnoreCase("Potion")) itemToAdd = new Potion(20);
                    else if (itemName.equalsIgnoreCase("Attack Boost")) itemToAdd = new AttackBoost(10, 3);

                    if (itemToAdd != null) playerInventory.addItem(itemToAdd, quantity);
                }
            }

            frame.getContentPane().removeAll();
            frame.add(gamePanel, BorderLayout.CENTER);
            frame.setContentPane(gamePanel);
            frame.pack();
            frame.setLocationRelativeTo(null);
            frame.setVisible(true);
            gamePanel.startGameThread();
            gamePanel.requestFocusInWindow();

        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(frame, "Save data is corrupted (Number format). " + e.getMessage(), "Load Error", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        } catch (NullPointerException e) {
            JOptionPane.showMessageDialog(frame, "Save data is incomplete (Null pointer). " + e.getMessage(), "Load Error", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(frame, "An unexpected error occurred during loading: " + e.getMessage(), "Load Error", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }
}