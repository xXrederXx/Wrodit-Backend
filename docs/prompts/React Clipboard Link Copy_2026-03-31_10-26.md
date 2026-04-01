# Clipboard

- Exported: 1.4.2026, 11:15:55
- Source: https://chatgpt.com/c/69b11722-b330-832c-8865-a70931a56f5
- Messages: 1



**You:**
wie kann man einen link ins clipboard kopieren
mit react

* * *

**ChatGPT:**

Wenn du **in React den aktuellen Link (URL) ins Clipboard kopieren** willst, geht das am einfachsten über die Browser-API `navigator.clipboard`.

Hier ein simples Beispiel:

```JavaScript
function CopyLinkButton() {  
  const copyToClipboard = async () => {  
    try {  
      await navigator.clipboard.writeText(window.location.href);  
      console.log("Link kopiert!");  
    } catch (err) {  
      console.error("Fehler beim Kopieren:", err);  
    }  
  };  
  
  return (  
    <button onClick={copyToClipboard}>  
      Link kopieren  
    </button>  
  );  
}  
  
export default CopyLinkButton;
```

### 🔑 Erklärung:

* `window.location.href` → aktueller URL
* `navigator.clipboard.writeText(...)` → kopiert Text ins Clipboard
* funktioniert nur bei **HTTPS oder localhost**

---

### Alternative (Fallback für ältere Browser):

Falls Clipboard API nicht geht:

```JavaScript
const copyFallback = () => {  
  const textarea = document.createElement("textarea");  
  textarea.value = window.location.href;  
  document.body.appendChild(textarea);  
  textarea.select();  
  document.execCommand("copy");  
  document.body.removeChild(textarea);  
};
```

---

### 💡 Bonus (mit React Router)

Wenn du z. B. `react-router` nutzt:

```JavaScript
import { useLocation } from "react-router-dom";  
  
function CopyLinkButton() {  
  const location = useLocation();  
  
  const copy = () => {  
    const url = window.location.origin + location.pathname;  
    navigator.clipboard.writeText(url);  
  };  
  
  return <button onClick={copy}>Link kopieren</button>;  
}
```

---

Wenn du willst, kann ich dir noch:

* Toast/Popup „Kopiert!“ anzeigen
* Button mit Icon machen
* oder das Ganze in deine bestehende Komponente einbauen 👍