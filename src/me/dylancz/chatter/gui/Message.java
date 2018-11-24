package me.dylancz.chatter.gui;

import javax.swing.*;
import java.awt.*;
import java.util.Date;

public abstract class Message {

    protected final Date timestamp;

    public Message(final Date timestamp) {
        this.timestamp = timestamp;
    }

    public abstract String getHeader();

    public abstract String getBody();


    public JComponent createComponent() {
        final JPanel messagePanel = new JPanel(new GridBagLayout());
        messagePanel.setOpaque(true);
        messagePanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(Color.GRAY),
            BorderFactory.createEmptyBorder(5, 5, 5, 5)
        ));
        messagePanel.setMinimumSize(new Dimension(496, 50));
        messagePanel.setPreferredSize(new Dimension(496, 50));
        messagePanel.setMaximumSize(new Dimension(496, 50));

        GridBagConstraints c = new GridBagConstraints();
        final JPanel namePanel = new JPanel();
        namePanel.setOpaque(false);
        c.gridx = 0;
        c.gridy = 0;
        c.anchor = GridBagConstraints.NORTHWEST;
        namePanel.add(new JLabel(this.getHeader()));
        messagePanel.add(namePanel, c);

        final JPanel contentPanel = new JPanel();
        contentPanel.setOpaque(false);
        c.gridx = 0;
        c.gridy = 1;
        c.anchor = GridBagConstraints.CENTER;
        final JTextArea textArea = new JTextArea(this.getBody());
        textArea.setOpaque(false);
        textArea.setEditable(false);
        textArea.setLineWrap(true);
        textArea.setWrapStyleWord(true);
        textArea.setSize(new Dimension(450, 50));
        contentPanel.add(textArea);
        messagePanel.add(contentPanel, c);

        return messagePanel;
    }

    public Date getTimestamp() {
        return this.timestamp;
    }

}
