# Injecting JWT Secret.md

- Exported: 20.03.2026,  7:58:11
- Source: https://chatgpt.com/c/69bc0bc9-39c8-8433-9a06-d1ad4ca75f21
- Messages 1

**You:**

ich bin auf einer detailseite mit einer id ich will jetzt die inhalte dieser seite laden wie bekomme ich die id für meine fetchanfrage mit react

* * *

**ChatGPT:**

Alles klar! Du bist auf einer Detailseite (z. B. `/posts/123`) und willst die Inhalte für diese bestimmte ID laden. In **React** hängt die Lösung davon ab, welches Routing du verwendest. Ich erkläre dir den gängigsten Weg mit **React Router v6**, der aktuell am meisten genutzt wird.

---

### 1️⃣ ID aus der URL holen

Wenn deine Route z. B. so aussieht:

```JavaScript
<Route path="/posts/:id" element={<PostDetail />} />
```

Dann kannst du in deiner `PostDetail`-Komponente die **ID** so bekommen:

```JavaScript
import { useParams } from "react-router-dom";  
  
function PostDetail() {  
  const { id } = useParams(); // id = "123" wenn URL /posts/123  
  console.log(id);  
  
  return <div>Post ID: {id}</div>;  
}

`useParams` liefert ein Objekt mit allen Parametern aus der URL.
```
---

### 2️⃣ Fetch-Anfrage mit der ID

Jetzt kannst du die ID direkt für deine API-Abfrage nutzen, z. B.:

```JavaScript
import { useEffect, useState } from "react";  
import { useParams } from "react-router-dom";  
  
function PostDetail() {  
  const { id } = useParams();  
  const [post, setPost] = useState(null);  
  const [loading, setLoading] = useState(true);  
  
  useEffect(() => {  
    async function fetchPost() {  
      try {  
        const res = await fetch(`https://api.example.com/posts/${id}`);  
        const data = await res.json();  
        setPost(data);  
      } catch (error) {  
        console.error("Fehler beim Laden des Posts:", error);  
      } finally {  
        setLoading(false);  
      }  
    }  
  
    fetchPost();  
  }, [id]); // useEffect läuft erneut, wenn sich die ID ändert  
  
  if (loading) return <p>Lädt...</p>;  
  if (!post) return <p>Post nicht gefunden</p>;  
  
  return (  
    <div>  
      <h1>{post.title}</h1>  
      <p>{post.content}</p>  
    </div>  
  );  
}
```
---

✅ **Wichtig:**

1. `useParams()` liefert **Strings**, also bei Zahlen ggf. `parseInt(id)` benutzen.
2. Immer `useEffect` nutzen, damit der Fetch beim Laden der Seite passiert.
3. Den Status (`loading`, `error`, `data`) verwalten, damit die UI sauber reagiert.

---

Wenn du willst, kann ich dir direkt ein **komplettes React-Beispiel für eine Detailseite** mit Loading, Error Handling und Fetch schreiben, das du copy-pasten kannst.

Willst du, dass ich das mache?