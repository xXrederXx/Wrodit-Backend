# Paralel Fetch

- Exported: 19.3.2026, 01:38:47
- Source: https://chatgpt.com/c/69bbc1f6-0378-8331-a359-ea49d99f4e66
- Messages: 2

**You:**

async function clientLoader() {
  return await fetchPosts();
} ich will das es 2 verschiedene fetchanfragen audführt und in 2 verschiedenen variablen speichert

* * *

**ChatGPT:**

Alles klar! Du willst also **zwei verschiedene `fetch`-Anfragen gleichzeitig ausführen** und die Ergebnisse in zwei Variablen speichern. In React Router Loader oder allgemein in async/await geht das so:

JavaScriptasync function clientLoader() {  
  // Erste Fetch-Anfrage  
  const posts = await fetchPosts();  
  
  // Zweite Fetch-Anfrage  
  const users = await fetchUser(); // oder eine andere Fetch-Funktion  
  
  // Beide Ergebnisse in einem Objekt zurückgeben  
  return { posts, users };  
}

Wenn du willst, dass die beiden Anfragen **parallel** laufen (also schneller, da sie nicht nacheinander warten), kannst du `Promise.all` verwenden:

JavaScriptasync function clientLoader() {  
  const [posts, users] = await Promise.all([fetchPosts(), fetchUser()]);  
  return { posts, users };  
}

Dann kannst du im Component so darauf zugreifen:

JavaScriptexport default function WroditHomeRoute() {  
  const { posts, users } = useLoaderData();  
  
  console.log(posts, users);  
  
  return (  
    <div>  
      <h1>Posts</h1>  
      {posts.map(post => (  
        <div key={post.id}>{post.title}</div>  
      ))}  
    </div>  
  );  
}

✅ Vorteil von `Promise.all`: beide Fetches starten gleichzeitig, dadurch sparst du Zeit.

Wenn du willst, kann ich direkt ein **komplettes Beispiel einbauen, bei dem `posts` die Postdaten enthält und `users` die Usernamen anhand der `userId` automatisch zuordnet**.

Willst du, dass ich das mache?

* * *

**You:**

export default function WroditHomeRoute() {
  const data = useLoaderData();
  const posts = data.content;
  console.log(data); wie kann ich diese daten jetzt aus useloderdata laden 

* * *

**ChatGPT:**

Ah, jetzt ist klar: du benutzt aktuell `useLoaderData()` und greifst nur auf `data.content` zu. Wenn du **zwei verschiedene Fetches** im Loader hast, sieht es ein bisschen anders aus. Ich erkläre Schritt für Schritt.

Angenommen, dein Loader macht jetzt zwei Fetches:

```JavaScript
// lib/wrodit.js  
export async function fetchPosts() {  
  const res = await fetch("/api/posts");  
  return res.json();  
}  
  
export async function fetchUsers() {  
  const res = await fetch("/api/users");  
  return res.json();  
}
```

Dann im Loader:

```JavaScript
// Route-Datei  
import { fetchPosts, fetchUsers } from "../lib/wrodit";  
  
export async function clientLoader() {  
  // beide parallel laden  
  const [posts, users] = await Promise.all([fetchPosts(), fetchUsers()]);  
  
  return { posts, users }; // return beide zusammen  
}
```


Jetzt im Component:

```JavaScriptimport 
{ useLoaderData } from "react-router";  
  
export default function WroditHomeRoute() {  
  const data = useLoaderData();  
  const posts = data.posts; // alle Posts  
  const users = data.users; // alle User  
  
  console.log("Posts:", posts);  
  console.log("Users:", users);  
  
  return (  
    <div>  
      <h1>Posts</h1>  
      {posts.map(post => {  
        // den Usernamen anhand userId suchen  
        const user = users.find(u => u.id === post.userId);  
        return (  
          <div key={post.id}>  
            <h2>{post.title}</h2>  
            <p>{post.content}</p>  
            <p>Autor: {user ? user.name : "Unbekannt"}</p>  
          </div>  
        );  
      })}  
    </div>  
  );  
}
```

**Wichtig:**

* `useLoaderData()` gibt genau das zurück, was dein Loader zurückgibt.
* Du kannst also `data.posts` und `data.users` direkt nutzen.
* Wenn du Usernamen für Posts anzeigen willst, musst du sie anhand `userId` matchen (wie oben mit `find`).

Wenn du willst, kann ich dir noch eine **optimierte Version mit `map` und direkter Zuordnung der User zu Posts** schreiben, damit das nicht bei jedem Render `find` macht.

Willst du, dass ich das mache?