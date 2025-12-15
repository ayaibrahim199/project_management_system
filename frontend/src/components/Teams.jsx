import React, { useEffect, useState } from 'react'

export default function Teams(){
  const [teams, setTeams] = useState([])
  const [loading, setLoading] = useState(true)
  const [error, setError] = useState(null)

  useEffect(() => {
    async function fetchTeams(){
      try {
        const res = await fetch('/api/teams')
        if (!res.ok) throw new Error(`API error ${res.status}`)
        const data = await res.json()
        setTeams(data)
      } catch (e) {
        setError(e.message)
      } finally {
        setLoading(false)
      }
    }
    fetchTeams()
  }, [])

  return (
    <div>
      <div className="flex items-center justify-between bg-gradient-to-r from-blue-500 to-purple-600 text-white p-6 rounded-lg shadow">
        <h1 className="text-2xl font-bold">👥 All Teams</h1>
        <a href="/teams/new" className="bg-white text-blue-600 font-semibold px-4 py-2 rounded shadow-sm hover:shadow">+ Create New Team</a>
      </div>

      {loading && <div className="mt-6 text-center text-gray-600">Loading teams…</div>}
      {error && <div className="mt-6 text-center text-red-600">{error}</div>}

      <div className="mt-6 grid gap-6 grid-cols-1 sm:grid-cols-2 lg:grid-cols-3">
        {teams.map(t => (
          <article key={t.id} className="bg-white rounded-lg shadow-md border-l-4 border-blue-200 p-5 flex flex-col">
            <header className="flex items-center justify-between mb-2">
              <h2 className="text-lg font-semibold">{t.name}</h2>
              <span className="text-sm text-gray-500 bg-gray-100 px-2 py-1 rounded">Team</span>
            </header>
            <p className="text-sm text-gray-600 mb-4">{t.description}</p>

            <div className="flex gap-4 mb-3">
              <div>
                <div className="text-lg font-bold">{t.members}</div>
                <div className="text-xs text-gray-500">Members</div>
              </div>
              <div>
                <div className="text-lg font-bold">{t.projects}</div>
                <div className="text-xs text-gray-500">Projects</div>
              </div>
            </div>

            <div className="text-sm text-gray-800 mb-4"><strong>Lead:</strong> {t.lead}</div>

            <footer className="mt-auto flex gap-2">
              <a className="px-3 py-1 text-sm border rounded text-blue-600" href={`/teams/${t.id}`}>View</a>
              <a className="px-3 py-1 text-sm border rounded" href={`/teams/edit/${t.id}`}>Edit</a>
              <form action={`/teams/delete/${t.id}`} method="post" className="ml-auto w-28">
                <button className="w-full bg-red-500 text-white text-sm py-1 rounded" type="submit" onClick={(e)=>{ if(!confirm('Delete this team?')) e.preventDefault()}}>Delete</button>
              </form>
            </footer>
          </article>
        ))}
      </div>
    </div>
  )
}
